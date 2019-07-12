package vc.thinker.utils;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author YanZhuoMin
 *
 * @date 2019年7月10日
 */
public class RedisCacheUtils {
	private JedisPool pool;
    private String SYS_CACHE;

    public RedisCacheUtils(String prefix, JedisPool pool) {
        this.SYS_CACHE = prefix;
        this.pool = pool;
    }

    public RedisCacheUtils(JedisPool pool) {
        this.SYS_CACHE = "";
        this.pool = pool;
    }

    public RedisCacheUtils(String prefix, String host, int port) {
        this.SYS_CACHE = prefix;
        this.pool = new JedisPool(host, port);
    }

    public Jedis getJedis() {
        Jedis jedis = this.pool.getResource();
        return jedis;
    }

    public void returnResource(Jedis jedis) {
        jedis.close();
    }

    public <T> T get(String key, TypeReference<T> type) {
        String str = this.get(key);
        return StringUtils.isBlank(str) ? null : JSON.parseObject(str, type, new Feature[0]);
    }

    public String get(String key) {
        Jedis jedis = this.pool.getResource();

        String var3;
        try {
            var3 = jedis.get(this.makeKey(key));
        } finally {
            jedis.close();
        }

        return var3;
    }

    public Long ttl(String key) {
        Jedis jedis = this.pool.getResource();

        Long var3;
        try {
            var3 = jedis.ttl(this.makeKey(key));
        } finally {
            jedis.close();
        }

        return var3;
    }

    public Long incr(String key) {
        Jedis jedis = this.pool.getResource();

        Long var3;
        try {
            var3 = jedis.incr(this.makeKey(key));
        } finally {
            jedis.close();
        }

        return var3;
    }

    public void set(String key, Object value) {
        this.set(key, JSON.toJSONString(value));
    }

    public void set(String key, String value) {
        Jedis jedis = this.pool.getResource();

        try {
            jedis.set(this.makeKey(key), value);
        } finally {
            jedis.close();
        }

    }

    public void set(String key, Object value, int expire) {
        this.set(key, JSON.toJSONString(value), expire);
    }

    public void set(String key, String value, int expire) {
        Jedis jedis = this.pool.getResource();

        try {
            jedis.setex(this.makeKey(key), expire, value);
        } finally {
            jedis.close();
        }

    }

    public void zadd(String key, double score, String member) {
        Jedis jedis = this.pool.getResource();

        try {
            jedis.zadd(key, score, member);
        } finally {
            this.pool.returnResource(jedis);
        }

    }

    public long zcount(String key) {
        Jedis jedis = this.pool.getResource();

        long var3;
        try {
            var3 = jedis.zcount(key, "-inf", "+inf");
        } finally {
            this.pool.returnResource(jedis);
        }

        return var3;
    }

    public Double zscore(String key, String member) {
        Jedis jedis = this.pool.getResource();

        Double var4;
        try {
            var4 = jedis.zscore(key, member);
        } finally {
            this.pool.returnResource(jedis);
        }

        return var4;
    }

    public long zrem(String key, String... members) {
        Jedis jedis = this.pool.getResource();

        long var4;
        try {
            var4 = jedis.zrem(key, members);
        } finally {
            this.pool.returnResource(jedis);
        }

        return var4;
    }

    public Set<String> zrevrangeAll(String key) {
        Jedis jedis = this.pool.getResource();

        Set var3;
        try {
            var3 = jedis.zrevrangeByScore(key, "+inf", "-inf");
        } finally {
            this.pool.returnResource(jedis);
        }

        return var3;
    }

    public Set<String> zrangeByScore(String key, String min, String max) {
        Jedis jedis = this.pool.getResource();

        Set var5;
        try {
            var5 = jedis.zrangeByScore(key, min, max);
        } finally {
            this.pool.returnResource(jedis);
        }

        return var5;
    }

    public boolean exists(String key) {
        Jedis jedis = this.pool.getResource();

        boolean var3;
        try {
            var3 = jedis.exists(this.makeKey(key));
        } finally {
            this.pool.returnResource(jedis);
        }

        return var3;
    }

    public void sadd(String key, String[] value, int expire) {
        Jedis jedis = this.pool.getResource();

        try {
            jedis.sadd(this.makeKey(key), value);
            this.expire(key, expire);
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        } finally {
            this.pool.returnResource(jedis);
        }

    }

    public void expire(String key, int expire) {
        Jedis jedis = this.pool.getResource();

        try {
            if (expire != 0) {
                jedis.expire(this.makeKey(key), expire);
            }
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        } finally {
            this.pool.returnResource(jedis);
        }

    }

    public long del(String key) {
        Jedis jedis = this.pool.getResource();

        long var3;
        try {
            var3 = jedis.del(this.makeKey(key));
        } finally {
            this.pool.returnResource(jedis);
        }

        return var3;
    }

    public long delAll(String pattern) {
        Jedis jedis = this.pool.getResource();

        try {
            StringBuilder patternSb = (new StringBuilder()).append(this.SYS_CACHE).append(":").append(pattern);
            Set<String> keys = jedis.keys(patternSb.toString());
            long result = 0L;

            String key;
            for(Iterator var7 = keys.iterator(); var7.hasNext(); result += jedis.del(key)) {
                key = (String)var7.next();
            }

            long var12 = result;
            return var12;
        } finally {
            this.pool.returnResource(jedis);
        }
    }

    public String makeKey(String key) {
        return StringUtils.isNotBlank(this.SYS_CACHE) ? this.SYS_CACHE + ":" + key : key;
    }

    public Set<String> keys(String pattern) {
        Jedis jedis = this.pool.getResource();

        Set var3;
        try {
            var3 = jedis.keys(pattern);
        } finally {
            this.pool.returnResource(jedis);
        }

        return var3;
    }
}
