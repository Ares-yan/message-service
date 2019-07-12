package vc.thinker;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import vc.thinker.utils.RedisCacheUtils;

/**
 * @author YanZhuoMin
 *
 * @date 2019年7月10日
 */
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.namespace}")
    private String namespace;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.oauthDatabase}")
    private int oauthdatabase;

    @Bean
    public RedisCacheUtils cacheUtils(@Qualifier("jedisPool") JedisPool jedisPool) {
        return new RedisCacheUtils(namespace, jedisPool);
    }

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMinIdle(2);
        poolConfig.setMaxIdle(10);
        JedisPool pool = new JedisPool(poolConfig, host, port, timeout, password, database);
        return pool;
    }

//    @Bean
//    public TokenStore tokenStore() {
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
//        config.setHostName(host);
//        config.setPort(port);
//        config.setPassword(RedisPassword.of(password));
//        config.setDatabase(oauthdatabase);
//        JedisConnectionFactory factory = new JedisConnectionFactory(config);
//        factory.afterPropertiesSet();
//        return new RedisTokenStore(factory);
//    }


    /**
     * 消息推送类执行类
     *
     * @return
     */
//    @Bean
//    public RedisMessageHandler redisMessageHandler() {
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(10);
//        poolConfig.setMinIdle(2);
//        poolConfig.setMaxIdle(5);
//        JedisPool pool = new JedisPool(poolConfig, host, port, timeout, password, database);
//        RedisMessageHandler handler = new RedisMessageHandler(pool);
//        return handler;
//    }

}
