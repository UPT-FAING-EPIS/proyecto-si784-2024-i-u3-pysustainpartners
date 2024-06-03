package com.loscuchurrumines.config;

import redis.clients.jedis.Jedis;

public class RedisConnection {

    private static Jedis jedis;
    public static void setJedis(Jedis jedis) {
        RedisConnection.jedis = jedis;
    }

    public static Jedis getConnection() {
        if (jedis == null) {
            jedis = new Jedis("localhost", 6379);
            jedis.auth("upt2023");
        }
        return jedis;
    }

    private RedisConnection() {
        throw new IllegalStateException("Utility class");
    }
}
