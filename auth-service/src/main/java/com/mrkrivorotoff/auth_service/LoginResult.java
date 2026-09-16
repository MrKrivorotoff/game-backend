package com.mrkrivorotoff.auth_service;

public sealed interface LoginResult {
    record Success(long userId) implements LoginResult {
    }

    record InvalidLoginData() implements LoginResult {
    }

    record InvalidCredentials() implements LoginResult {
    }
}