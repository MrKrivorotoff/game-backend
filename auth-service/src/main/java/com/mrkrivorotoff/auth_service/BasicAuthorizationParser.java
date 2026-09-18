package com.mrkrivorotoff.auth_service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class BasicAuthorizationParser {
    private static final String BASIC_PREFIX = "Basic ";
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    private BasicAuthorizationParser() {
        throw new AssertionError();
    }

    public static BasicCredentials parse(String authorization) {
        if (authorization == null || !authorization.startsWith(BASIC_PREFIX)) {
            throw new IllegalArgumentException("Invalid authorization scheme");
        }

        var encoded = authorization.substring(BASIC_PREFIX.length());

        var decoded = (byte[]) null;
        try {
            decoded = DECODER.decode(encoded);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 credentials", e);
        }

        var credentials = new String(decoded, StandardCharsets.UTF_8);

        var separator = credentials.indexOf(':');
        if (separator < 0) {
            throw new IllegalArgumentException("Invalid Basic credentials");
        }

        var username = credentials.substring(0, separator);
        var password = credentials.substring(separator + 1);
        return new BasicCredentials(username, password);
    }
}