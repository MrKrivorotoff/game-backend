package com.mrkrivorotoff.login_service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public final class LoginService {
    public String getAuthToken() {
        return UUID.randomUUID().toString();
    }
}