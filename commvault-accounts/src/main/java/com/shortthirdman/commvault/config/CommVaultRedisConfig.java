// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.Executors;

import static com.shortthirdman.commvault.common.CommVaultConstants.COLON;
import static com.shortthirdman.commvault.common.CommVaultConstants.REDIS_PROTOCOL_PREFIX;

@Slf4j
@Configuration
public class CommVaultRedisConfig {

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config redissonConfig = new Config()
//                .setCodec(ProtobufRedisCodec.INSTANCE)
                .setExecutor(Executors.newSingleThreadExecutor(new CustomizableThreadFactory("RedisExecutor-")));

        if (redisProperties.getHost() == null || redisProperties.getHost().isEmpty() || redisProperties.getPort() <= 0) {
            throw new IllegalArgumentException("Redis host or port is null or empty");
        }

        redissonConfig
                .useSingleServer()
                .setAddress(Objects.requireNonNullElse(
                        redisProperties.getUrl(),
                        REDIS_PROTOCOL_PREFIX + redisProperties.getHost() + COLON + redisProperties.getPort()
                ))
                .setDatabase(0)
                .setPassword(redisPassword);
        return Redisson.create(redissonConfig);
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setDatabase(0);
        redisStandaloneConfiguration.setPort(redisPort);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPassword));
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        jedisConnectionFactory.setAutoStartup(Boolean.FALSE);
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate() {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisKeyValueTemplate redisKeyValueTemplate(final RedisOperations<String, Serializable> redisOperations) {
        return new RedisKeyValueTemplate((RedisKeyValueAdapter) redisOperations, new RedisMappingContext());
    }
}
