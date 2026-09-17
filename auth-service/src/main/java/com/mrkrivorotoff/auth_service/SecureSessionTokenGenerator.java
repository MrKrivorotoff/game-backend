package com.mrkrivorotoff.auth_service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

@Component
public final class SecureSessionTokenGenerator implements SessionTokenGenerator {
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

    private final Random random = new SecureRandom();

    @Override
    public String generate() {
        var bytes = new byte[32];
        random.nextBytes(bytes);
        return ENCODER.encodeToString(bytes);
    }
}