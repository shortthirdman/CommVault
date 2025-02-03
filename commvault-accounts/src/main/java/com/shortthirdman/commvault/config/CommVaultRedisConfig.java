// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.config;

import com.shortthirdman.commvault.model.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.Objects;
import java.util.concurrent.Executors;

import static com.shortthirdman.commvault.common.CommVaultConstants.COLON;

@Slf4j
@Configuration
public class CommVaultRedisConfig {

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

//    @Bean(destroyMethod = "shutdown")
//    public RedissonClient redissonClient(RedisProperties redisProperties) {
//        Config redissonConfig = new Config()
//                //.setCodec(ProtobufRedisCodec.INSTANCE)
//                .setExecutor(Executors.newSingleThreadExecutor(new CustomizableThreadFactory("RedisExecutor-")));
//
//        redissonConfig
//                .useSingleServer()
//                .setAddress(Objects.requireNonNullElse(
//                        redisProperties.getUrl(),
//                        "redis://" + redisProperties.getHost() + COLON + redisProperties.getPort()
//                ));
//        return Redisson.create(redissonConfig);
//    }

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

    /*@Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration(redisHost, redisPort);
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfig =
                JedisClientConfiguration.builder();
        jedisClientConfig.connectTimeout(Duration.ofMillis(10000));
        jedisClientConfig.usePooling();
        jedisClientConfig.useSsl();
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPassword));
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfig.build());
    }*/

    @Bean
    public RedisTemplate<String, UserAccount> redisTemplate() {
        RedisTemplate<String, UserAccount> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
