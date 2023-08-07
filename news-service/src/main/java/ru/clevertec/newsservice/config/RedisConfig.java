package ru.clevertec.newsservice.config;

//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfig {

//    @Bean
//    public RedissonClient initRedissonClient(@Value("${spring.redis.host}") String host,
//                                             @Value("${spring.redis.port}") String port) {
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://" + host + ":" + port);
//        return Redisson.create(config);
//    }

    @Bean
    @Primary
    public RedisCacheManager redisCacheManager(
                                               RedisConnectionFactory connectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);
        RedisSerializationContext.SerializationPair<Object> valueSerializationPair
                = RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        cacheConfiguration = cacheConfiguration.serializeValuesWith(valueSerializationPair);

        RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter, cacheConfiguration);
        return redisCacheManager;
    }
}
