package com.mrkrivorotoff.inventory_service;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(schema = "public", name = "user_balances")
@IdClass(UserCurrencyId.class)
public class UserCurrency {
    private Long userId;
    private String currencyId;
    private long amount;

    public UserCurrency() {
    }

    @Id
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public UserCurrency setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    @Id
    @Column(name = "currency_id")
    public String getCurrencyId() {
        return currencyId;
    }

    public UserCurrency setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
        return this;
    }

    public long getAmount() {
        return amount;
    }

    public UserCurrency setAmount(long amount) {
        this.amount = amount;
        return this;
    }
}