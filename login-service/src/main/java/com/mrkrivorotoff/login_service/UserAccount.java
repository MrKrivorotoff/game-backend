package com.mrkrivorotoff.login_service;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public record UserAccount(
        @Id long id,
        @Column("username") String username,
        @Column("password_hash") String passwordHash
) {
}