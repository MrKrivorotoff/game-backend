package com.mrkrivorotoff.auth_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public final class SessionService {
    private final SessionTokenGenerator sessionTokenGenerator;
    private final ValueOperations<String, String> redisValueOperations;

    @Autowired
    public SessionService(SessionTokenGenerator sessionTokenGenerator, StringRedisTemplate redisTemplate) {
        this.sessionTokenGenerator = requireNonNull(sessionTokenGenerator);
        this.redisValueOperations = redisTemplate.opsForValue();
    }

    public String createUserSession(long userId) {
        var sessionToken = sessionTokenGenerator.generate();
        redisValueOperations.set(sessionToken, Long.toString(userId));
        return sessionToken;
    }
}