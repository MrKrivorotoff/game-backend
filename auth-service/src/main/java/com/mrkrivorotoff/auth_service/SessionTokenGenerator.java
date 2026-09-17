package com.mrkrivorotoff.auth_service;

@FunctionalInterface
public interface SessionTokenGenerator {
    String generate();
}