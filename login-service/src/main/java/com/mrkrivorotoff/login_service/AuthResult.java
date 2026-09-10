package com.mrkrivorotoff.login_service;

public sealed interface AuthResult {
    record Success(String authToken) implements AuthResult {
    }

    record InvalidRequest() implements AuthResult {
    }

    record InvalidCredentials() implements AuthResult {
    }
}