package com.mrkrivorotoff.login_service;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    @Query("SELECT * FROM users WHERE LOWER(username) = LOWER(:username)")
    Optional<UserAccount> findByUsernameIgnoreCase(@Param("username") String username);
}