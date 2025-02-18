// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.shortthirdman.commvault.common.CommVaultConstants.ASTERISK;
import static com.shortthirdman.commvault.common.CommVaultConstants.COLON;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommVaultAdaptor {

    private final RedisTemplate<String, Serializable> redisTemplate;

    public boolean lock(String key, Object value, long timeout) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, (Serializable) value, timeout, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    public void saveData(String key, Object value) {
        redisTemplate.opsForValue().set(key, (Serializable) value);
    }

    public Boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }

    public Boolean delete(String key) {
        return redisTemplate.opsForValue().getOperations().delete(key);
    }

    public void saveData(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, (Serializable) value, timeout, TimeUnit.MINUTES);
    }

    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Set<String> cacheKeys(String prefix) {
        StringBuilder cacheKey = new StringBuilder(prefix);
        cacheKey.append(COLON);
        cacheKey.append(ASTERISK);

        var keys = redisTemplate.keys(cacheKey.toString());
        log.info("Found {} keys with prefix pattern {}", keys.size(), prefix);

        if (keys.isEmpty()) {
            throw new IllegalStateException("Cache keys is empty");
        }

        return keys;
    }
}
