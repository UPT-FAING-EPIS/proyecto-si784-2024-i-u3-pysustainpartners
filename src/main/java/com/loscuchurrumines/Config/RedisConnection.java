package com.loscuchurrumines.Config;

import redis.clients.jedis.Jedis;

public class RedisConnection {
    private static Jedis jedis;
    static {
        jedis = new Jedis("localhost", 6379);
        jedis.auth("upt2023");
    }
    
    public static Jedis getConnection() {
        return jedis;
    }
}
