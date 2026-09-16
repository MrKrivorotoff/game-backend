package com.mrkrivorotoff.auth_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

@Service
public final class AuthService {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("[A-Za-z0-9_]{3,32}");

    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserAccountRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = requireNonNull(userRepository);
        this.passwordEncoder = requireNonNull(passwordEncoder);
    }

    public LoginResult login(String username, String password) {
        if (username.isBlank() || password.isEmpty())
            return new LoginResult.InvalidLoginData();
        var foundUser = userRepository.findByUsernameIgnoreCase(username);
        if (foundUser.isPresent()) {
            var userAccount = foundUser.get();
            if (passwordEncoder.matches(password, userAccount.passwordHash()))
                return new LoginResult.Success(userAccount.id());
        }
        return new LoginResult.InvalidCredentials();
    }

    public RegistrationResult registerNewUser(String username, String password) {
        if (!USERNAME_PATTERN.matcher(username).matches())
            return RegistrationResult.INVALID_REGISTRATION_DATA;
        var passwordLength = password.length();
        if (passwordLength < 8 || passwordLength > 128)
            return RegistrationResult.INVALID_REGISTRATION_DATA;
        var foundUser = userRepository.findByUsernameIgnoreCase(username);
        if (foundUser.isPresent())
            return RegistrationResult.CONFLICT;
        var user = userRepository.save(new UserAccount(null, username, passwordEncoder.encode(password)));
        try {
            userRepository.save(user);
        } catch (DuplicateKeyException e) {
            return RegistrationResult.CONFLICT;
        }
        return RegistrationResult.SUCCESS;
    }
}