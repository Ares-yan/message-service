package vc.thinker.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * @author YanZhuoMin
 *
 * @date 2019年7月10日
 */
public class JedisHolder {
	private JedisPoolConfig jedisPoolConfig;

	private JedisPool jedisPool;

	private final ConcurrentMap<String, JedisPool> poolMap = new ConcurrentHashMap<String, JedisPool>();

	private static JedisHolder holder = new JedisHolder();

	private JedisHolder() {
		initJedisPoolConfig();
	}

	public static JedisHolder singleton() {
		return holder;
	}

	private void initJedisPoolConfig() {
		jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(30);
		jedisPoolConfig.setMaxIdle(8);
	}

	private JedisPool getJedisPool(String key, JedisPool jedisPool) {
		if (poolMap.get(key) == null) {
			poolMap.putIfAbsent(key, jedisPool);
		}
		return poolMap.get(key);
	}

	public JedisPool getJedisPoolInstance(String host) {
		return getJedisPoolInstance(host, Protocol.DEFAULT_PORT);
	}

	public JedisPool getJedisPoolInstance(String host, int port) {
		StringBuilder sb = new StringBuilder();
		sb.append(host).append(port);
		jedisPool = new JedisPool(jedisPoolConfig, host, port);
		return this.getJedisPool(sb.toString(), jedisPool);
	}

	/** redis默认的超时时间是2秒 */
	public JedisPool getJedisPoolInstance(String host, int port, int timeout) {
		StringBuilder sb = new StringBuilder();
		sb.append(host).append(port).append(timeout);
		jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);

		return this.getJedisPool(sb.toString(), jedisPool);
	}

	public JedisPool getJedisPoolInstance(String host, int port, int timeout, String password) {
		StringBuilder sb = new StringBuilder();
		sb.append(host).append(port).append(timeout).append(password);
		jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
		return this.getJedisPool(sb.toString(), jedisPool);
	}

	public JedisPool getJedisPoolInstance(String host, int port, int timeout, String password, int database) {
		StringBuilder sb = new StringBuilder();
		sb.append(host).append(port).append(timeout).append(password).append(database);
		jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);

		return this.getJedisPool(sb.toString(), jedisPool);
	}

	/*
	 * public Jedis getJedis() { return jedisPool.getResource(); } public boolean
	 * release(Jedis jedis) { if(null!=jedis&& null!=jedisPool) {
	 * jedisPool.returnResource(jedis); return true; } return false; }
	 */

	public boolean destoryPool() {
		if (null != jedisPool) {
			jedisPool.destroy();
			return true;
		}
		return false;
	}

	public interface RedisCallback<T> {

		T doInRedis(Jedis jedis);
	}

	public static <T> T execute(JedisPool jp, RedisCallback<T> r) {
		Jedis jedis = jp.getResource();
		try {
			return r.doInRedis(jedis);
		} finally {
			jp.returnResource(jedis);
		}
	}
}
