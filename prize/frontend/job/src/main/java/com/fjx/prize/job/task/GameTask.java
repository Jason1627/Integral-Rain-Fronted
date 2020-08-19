package com.fjx.prize.job.task;

import com.fjx.prize.commons.db.entity.*;
import com.fjx.prize.commons.config.RedisKeys;
import com.fjx.prize.commons.db.entity.*;
import com.fjx.prize.commons.db.mapper.CardGameMapper;
import com.fjx.prize.commons.db.mapper.CardGameProductMapper;
import com.fjx.prize.commons.db.mapper.CardGameRulesMapper;
import com.fjx.prize.commons.db.mapper.GameLoadMapper;
import com.fjx.prize.commons.utils.RedisUtil;
import com.fjx.prize.job.annotation.ElasticSimpleJob;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 1. 活动开始前一分钟扫描所有要开始的活动
 2. 将活动信息加载进redis
 3. 将活动策略信息加载进redis
 4. 按活动奖品信息，生成对应的个数的时间戳做令牌，从小到大排好顺序，右侧入队
 5. 以令牌为key，对应的奖品为value，建立映射关系，为中奖后获取奖品做准备
 6.抽奖开始，从令牌队列左侧获取令牌
 7.如果令牌小于当前时间，说明中奖，找到令牌对应的奖品，抽走即可
 8.如果令牌大于当前时间，说明未中奖，从左侧将令牌还回队列

 * 活动信息预热，每隔1分钟执行一次
 * 查找未来1分钟内（含），要开始的活动
 */
@Component
@ElasticSimpleJob(cron = "0 * * * * ?")
public class GameTask implements SimpleJob {
    private final static Logger log = LoggerFactory.getLogger(GameTask.class);
    @Autowired
    private CardGameMapper gameMapper;
    @Autowired
    private CardGameProductMapper gameProductMapper;
    @Autowired
    private CardGameRulesMapper gameRulesMapper;
    @Autowired
    private GameLoadMapper gameLoadMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void execute(ShardingContext shardingContext) {
        // 当前时间，进入方法的第一个时间，代码执行存在一定的时间，如果到了毫秒级别就会出错，所以当前时间一定是要在进入方法时的时间。
        Date now = new Date();
        // 创建活动对象
        CardGameExample example = new CardGameExample();
        // 查询将来1分钟内要开始的活动   （Criteria ： 是一个查询构建器）
        CardGameExample.Criteria criteria = example.createCriteria();
        // 开始时间大于当前时间 （andStarttimeGreaterThan  ：由三部分组成，分别是java 代码的中and  + 实体类中的字段Starttime + GreaterThan）
        criteria.andStarttimeGreaterThan(now);
        // 开始时间小于等于（当前时间+1分钟）  LessThanOrEqualTo ：小于等于
        //  这两段代码 就是开始时间大于当前时间，小于等于（当前时间+一分钟）
        criteria.andStarttimeLessThanOrEqualTo(DateUtils.addMinutes(now,1));
        // 获取活动列表
        List<CardGame> list = gameMapper.selectByExample(example);
        if(list.size() == 0){
            //没有查到要开始的活动
            log.info("活动个数 = 0");
            return;
        }
        // 查询到有将要开始的活动
        log.info("活动个数 = {}",list.size());
        // 有相关活动数据，则将活动数据预热，进redis
        list.forEach(game ->{
            //活动开始时间
            long start = game.getStarttime().getTime();
            //活动结束时间
            long end = game.getEndtime().getTime();
            //计算活动结束时间到现在还有多少秒，作为redis key过期时间
            long expire = (end - now.getTime())/1000;
            // long expire = -1; //永不过期
            //活动持续时间（ms）
            long duration = end - start;

            //活动基本信息，将活动状态设为1 表示已经加载，此时才可以将活动信息加载到redis缓存中
            game.setStatus(1);
            redisUtil.set(RedisKeys.INFO+game.getId(),game,-1);
            log.info("加载活动信息:{},{},{},{}", game.getId(),game.getTitle(),game.getStarttime(),game.getEndtime());

            // 根据活动id，加载活动奖品信息
            List<CardProductDto> products = gameLoadMapper.getByGameId(game.getId());
            Map<Integer, CardProduct> productMap = new HashMap<>(products.size());
            // 将奖品的id 和奖品的详细信息 以k-v 形式存放子productMap中
            products.forEach(product -> productMap.put(product.getId(),product));
            log.info("加载活动奖品信息:{}",productMap.size());

            //奖品数量等配置信息（活动id +商品id + 商品数量）
            CardGameProductExample productExample = new CardGameProductExample();
            productExample.createCriteria().andGameidEqualTo(game.getId());
            // 获取活动id +奖品id +奖品数量的中间表
            List<CardGameProduct> gameProducts = gameProductMapper.selectByExample(productExample);
            log.info("load bind product:{}",gameProducts.size());

            //令牌桶，令牌是一个long 类型的时间戳
            List<Long> tokenList = new ArrayList();
            gameProducts.forEach(cgp ->{
                // amount 是奖品的个数 ，有多个个奖品就有多少个令牌，一个令牌对应一个奖品
                // 生成amount个start到end之间的随机时间戳做令牌
                for (int i = 0; i < cgp.getAmount(); i++) {
                    // rnd = 活动的开始时间+活动的持续时间内的一个随机时间，此时就是一个令牌，代表该时刻发放一个奖品
                    // 此时存在一个问题，如果活动持续时间很短，但是奖品的数量很多，就会造成相同的时间有多个奖品，这就存在问题了
                    long rnd = start + new Random().nextInt((int)duration);
                    // 为什么乘1000，再额外加一个随机数呢？ - 防止时间段奖品多时重复
                    // 记得取令牌判断时间时，除以1000，还原真正的时间戳
                    long token = rnd * 1000 + new Random().nextInt(999);
                    // 将令牌放入令牌桶，此时令牌桶内的令牌是无序的
                    tokenList.add(token);
                    // 以令牌做key，对应的商品为value，创建redis缓存
                    log.info("令牌 -> 活动名称 : {} -> {}",token/1000 ,productMap.get(cgp.getProductid()).getName());
                    // token到实际奖品之间建立映射关系
                    // 以活动id+令牌为key,以商品的详细信息作为value
                    redisUtil.set(RedisKeys.TOKEN + game.getId() +"_"+token,productMap.get(cgp.getProductid()),expire);
                }
            });
            //排序后放入redis队列
            Collections.sort(tokenList);
            log.info("加载排序好的令牌:{}",tokenList);

            // 从右侧压入队列，从左到右，时间戳逐个增大
            redisUtil.rightPushAll(RedisKeys.TOKENS + game.getId(),tokenList);
            // 设置令牌的过期时间
            redisUtil.expire(RedisKeys.TOKENS + game.getId(),expire);

            //加载活动的奖品策略配置信息
            CardGameRulesExample rulesExample = new CardGameRulesExample();
            rulesExample.createCriteria().andGameidEqualTo(game.getId());
            List<CardGameRules> rules = gameRulesMapper.selectByExample(rulesExample);
            // 遍历策略，存入redis hset ,以活动id为组，用户等级为key，用户最大中奖次数或可抽奖次数为value
            // 不同等级的会员，可抽奖次数和最大中奖次数不同
            rules.forEach(r -> {
                // 最大中奖次数
                redisUtil.hset(RedisKeys.MAXGOAL +game.getId(),r.getUserlevel()+"",r.getGoalTimes());
                // 可抽奖的次数
                redisUtil.hset(RedisKeys.MAXENTER +game.getId(),r.getUserlevel()+"",r.getEnterTimes());
                log.info("加载活动规则:会员级别={},可抽奖此处={},最大中奖数={}",r.getUserlevel(),r.getEnterTimes(),r.getGoalTimes());
            });
            // 设置过期时间
            redisUtil.expire(RedisKeys.MAXGOAL +game.getId(),expire);
            redisUtil.expire(RedisKeys.MAXENTER +game.getId(),expire);

            //活动状态变更为已预热，禁止管理后台再随便变动
            game.setStatus(1);
            gameMapper.updateByPrimaryKey(game);
        });
    }
}
