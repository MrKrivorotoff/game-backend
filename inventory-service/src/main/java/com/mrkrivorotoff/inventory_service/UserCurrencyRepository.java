package com.mrkrivorotoff.inventory_service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCurrencyRepository extends JpaRepository<UserCurrency, UserCurrencyId> {
    @Query("SELECT userCurrency FROM UserCurrency userCurrency WHERE userCurrency.userId = :userId")
    List<UserCurrency> findByUserId(@Param("userId") Long userId);
}