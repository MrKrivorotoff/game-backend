package com.mrkrivorotoff.auth_service;

public record BasicCredentials(
        String username,
        String password
) {
}