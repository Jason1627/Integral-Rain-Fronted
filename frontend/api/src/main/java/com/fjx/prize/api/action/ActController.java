package com.fjx.prize.api.action;

import com.fjx.prize.api.config.LuaScript;
import com.fjx.prize.commons.config.RabbitKeys;
import com.fjx.prize.commons.config.RedisKeys;
import com.fjx.prize.commons.db.entity.*;
import com.fjx.prize.commons.db.mapper.CardGameMapper;
import com.fjx.prize.commons.utils.ApiResult;
import com.fjx.prize.commons.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/act")
@Api(tags = {"抽奖模块"})
public class ActController {

    @Autowired
    private CardGameMapper cardGameMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private LuaScript luaScript;


    @GetMapping("/go/{gameid}")
    @ApiOperation(value = "抽奖")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameid", value = "活动id", example = "1", required = true)
    })
    public ApiResult<Object> act(@PathVariable int gameid, HttpServletRequest request) {
        Date now = new Date();
        // 获取活动基本信息
        CardGame game = (CardGame) redisUtil.get(RedisKeys.INFO + gameid);
        // 判断活动是否开始，一种是活动还没有加载到redis ，一种是加载到redis，但是活动的开始时间还没有到，都是未开始
        // 如果活动信息还没加载进redis，无效
        // 如果活动已经加载，预热完成，但是开始时间 > 当前时间，也无效
        if (game == null || game.getStarttime().after(now)) {
            return new ApiResult(-1, "活动未开始", null);
        }
        //判断活动是否已结束
        if (now.after(game.getEndtime())) {
            return new ApiResult(-1, "活动已结束", null);
        }

        //获取当前用户，分布式session ，session 只存放用户的id
        HttpSession session = request.getSession();
        CardUser user = (CardUser) redisUtil.get(RedisKeys.SESSIONID + session.getId());
        if (user == null) {
            return new ApiResult(-1, "未登陆", null);
        } else {
            // 用户登陆，判断该用户是否参与了该活动，如果没有参与，绑定参与关系
            if (!redisUtil.hasKey(RedisKeys.USERGAME + user.getId() + "_" + gameid)) {
                redisUtil.set(RedisKeys.USERGAME + user.getId() + "_" + gameid, 1,
                        (game.getEndtime().getTime() - now.getTime()) / 1000);
                //持久化抽奖记录，扔给消息队列处理
                CardUserGame userGame = new CardUserGame();
                userGame.setUserid(user.getId());
                userGame.setGameid(gameid);
                userGame.setCreatetime(new Date());
                // 将消息发送给消息队列
                rabbitTemplate.convertAndSend(RabbitKeys.QUEUE_PLAY, userGame);
            }
        }
        //用户已中奖次数
        Integer count = (Integer) redisUtil.get(RedisKeys.USERHIT + gameid + "_" + user.getId());
        if (count == null) {
            // 用户第一进来，count 默认为null，设置默认值为 0
            count = 0;
            redisUtil.set(RedisKeys.USERHIT + gameid + "_" + user.getId(), count,
                    (game.getEndtime().getTime() - now.getTime()) / 1000);
        }
        //根据会员等级，获取本活动允许的最大中奖数
        Integer maxcount = (Integer) redisUtil.hget(RedisKeys.MAXGOAL + gameid, user.getLevel() + "");
        //如果没设置，默认为0，即：不限制次数
        maxcount = maxcount == null ? 0 : maxcount;
        //次数对比
        if (maxcount > 0 && count >= maxcount) {
            //如果达到最大次数，不允许抽奖
            return new ApiResult(-1, "您已达到最大中奖数", null);
        }

        /*
          取令牌，然后将令牌放入令牌桶存在并发问题，令牌桶中的令牌是有顺序的
          当多线程取令牌时，然后将令牌放回令牌桶的过程中可能出现乱序
          所以取令牌和放回令牌必须是原子性操作
          解决方法：
          1. 对代码块进行加锁
          2. redis +lua 脚本可以保证原子性
         */

//        //以上校验全部通过，准许进入抽奖逻辑
//        Long token = (Long) redisUtil.leftPop(RedisKeys.TOKENS + gameid);
//        if (token == null) {
//            //令牌已用光，说明奖品抽光了
//            return new ApiResult(-1, "奖品已抽光", null);
//        }
//
//        //判断令牌时间戳大小，即是否中奖
//        //这里记住，取出的令牌要除以1000，参考job项目，令牌生成部分
//        if (now.getTime() < token / 1000) {
//            //当前时间小于令牌时间戳，说明奖品未到发放时间点，放回令牌，返回未中奖
//            redisUtil.leftPush(RedisKeys.TOKENS + gameid, token);
//            return new ApiResult(0, "未中奖", null);
//        }


        Long token = luaScript.tokenCheck(RedisKeys.TOKENS + gameid, String.valueOf(now.getTime()));

        if(token==0){
            return new ApiResult(-1, "奖品已抽光", null);
        }else if (token==1){
            return new ApiResult(0, "未中奖", null);
        }

        //以上逻辑走完，说明很幸运，中奖了！
        //抽中的奖品：
        CardProduct product = (CardProduct) redisUtil.get(RedisKeys.TOKEN + gameid + "_" + token);
        //中奖次数加1,incr可以保证原子性
        redisUtil.incr(RedisKeys.USERHIT + gameid + "_" + user.getId(), 1);
        //投放消息给队列，中奖后的耗时业务，交给消息模块处理
        CardUserHit hit = new CardUserHit();
        hit.setGameid(gameid);
        hit.setHittime(now);
        hit.setProductid(product.getId());
        hit.setUserid(user.getId());
        // 将中奖信息发送到 QUEUE_HIT 队列
        rabbitTemplate.convertAndSend(RabbitKeys.EXCHANGE_DIRECT, RabbitKeys.QUEUE_HIT, hit);

        //返回给前台中奖信息
        return new ApiResult(1, "恭喜中奖", product);
    }

    @GetMapping("/info/{gameid}")
    @ApiOperation(value = "缓存信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameid", value = "活动id", example = "1", required = true)
    })
    public ApiResult info(@PathVariable int gameid) {
        Map map = new LinkedHashMap<>();
        // 存放活动基本信息
        map.put(RedisKeys.INFO + gameid, redisUtil.get(RedisKeys.INFO + gameid));
        // 获取令牌桶中的全部令牌
        List<Object> tokens = redisUtil.lrange(RedisKeys.TOKENS + gameid, 0, -1);
        // 将令牌桶中的全部令牌有序的存放在 map 中
        Map tokenMap = new LinkedHashMap();
        tokens.forEach(o -> tokenMap.put(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(Long.valueOf(o.toString()) / 1000)),
                // 获取奖品的具体信息
                redisUtil.get(RedisKeys.TOKEN + gameid + "_" + o))
        );
        // 存放活动的令牌信息
        map.put(RedisKeys.TOKENS + gameid, tokenMap);
        // 存放活动的最大中奖数和可中奖数
        map.put(RedisKeys.MAXGOAL + gameid, redisUtil.hmget(RedisKeys.MAXGOAL + gameid));
        map.put(RedisKeys.MAXENTER + gameid, redisUtil.hmget(RedisKeys.MAXENTER + gameid));
        // 返回缓存信息
        return new ApiResult(200, "缓存信息", map);
    }
}
