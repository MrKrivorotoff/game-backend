package com.mrkrivorotoff.login_service;

public sealed interface LoginResult {
    record Success(String authToken) implements LoginResult {
    }

    record InvalidLoginData() implements LoginResult {
    }

    record InvalidCredentials() implements LoginResult {
    }
}