package com.mrkrivorotoff.auth_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
public final class SessionService {
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public SessionService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = requireNonNull(redisTemplate);
    }

    public String createUserSession(long userId) {
        var sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(sessionId, String.valueOf(userId));
        return sessionId;
    }
}