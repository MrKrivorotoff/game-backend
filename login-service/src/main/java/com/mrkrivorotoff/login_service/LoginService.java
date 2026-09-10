package com.mrkrivorotoff.login_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
public final class LoginService {
    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(UserAccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = requireNonNull(repository);
        this.passwordEncoder = requireNonNull(passwordEncoder);
    }

    private static String createAuthToken() {
        return UUID.randomUUID().toString();
    }

    public AuthResult login(String username, String password) {
        if (username.isEmpty() || password.isEmpty())
            return new AuthResult.InvalidRequest();
        var foundUser = repository.findByUsername(username);
        if (foundUser.isEmpty() || !passwordEncoder.matches(password, foundUser.get().passwordHash()))
            return new AuthResult.InvalidCredentials();
        return new AuthResult.Success(createAuthToken());
    }
}