package com.mrkrivorotoff.inventory_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Service
public final class InventoryService {
    private final UserCurrencyRepository userCurrencyRepository;

    @Autowired
    public InventoryService(UserCurrencyRepository userCurrencyRepository) {
        this.userCurrencyRepository = requireNonNull(userCurrencyRepository);
    }

    private static Map<String, Long> convertUserCurrencies(Collection<UserCurrency> userCurrencies) {
        return userCurrencies.stream()
                .collect(Collectors.toMap(UserCurrency::getCurrencyId, UserCurrency::getAmount, Long::sum));
    }

    public Map<String, Long> getUserCurrencies(Long userId) {
        return convertUserCurrencies(userCurrencyRepository.findByUserId(userId));
    }
}