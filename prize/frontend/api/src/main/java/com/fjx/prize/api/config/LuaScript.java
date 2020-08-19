package com.fjx.prize.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LuaScript{
    @Autowired
    private RedisTemplate redisTemplate;
 
    private DefaultRedisScript<Long> script;

    // 定义一个初始化的方法
    @PostConstruct
    public void init(){
        script = new DefaultRedisScript<Long>();
        // 设置脚本类型为long类型
        script.setResultType(Long.class);
        // 加载脚本的内容，路径
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/tokenCheck.lua")));
    }
    // tokenCheck  方法去调用脚本
    // gamekey :  RedisKeys.TOKENS + gameid
    public Long tokenCheck(String gamekey,String curtime){

        List<String> keys = new ArrayList();
        keys.add(gamekey);
        keys.add(curtime);

        Long result = (Long) redisTemplate.execute(script,keys,0,0);

        return result;
    }
}