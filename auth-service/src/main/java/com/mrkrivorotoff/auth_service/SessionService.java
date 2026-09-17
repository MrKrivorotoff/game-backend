package com.mrkrivorotoff.auth_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public final class SessionService {
    private final SessionTokenGenerator sessionIdGenerator;
    private final ValueOperations<String, String> redisValueOperations;

    @Autowired
    public SessionService(SessionTokenGenerator sessionIdGenerator, StringRedisTemplate redisTemplate) {
        this.sessionIdGenerator = requireNonNull(sessionIdGenerator);
        this.redisValueOperations = redisTemplate.opsForValue();
    }

    public String createUserSession(long userId) {
        var sessionId = sessionIdGenerator.generate();
        redisValueOperations.set(sessionId, Long.toString(userId));
        return sessionId;
    }
}