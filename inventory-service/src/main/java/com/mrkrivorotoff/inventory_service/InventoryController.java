package com.mrkrivorotoff.inventory_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@Controller
@RequestMapping("inventory")
public final class InventoryController {
    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = requireNonNull(inventoryService);
    }

    @ResponseBody
    @GetMapping("user_currencies")
    public Map<String, Long> getUserCurrencies(@RequestHeader("X-User-Id") String userId) {
        log.info("GET inventory/user_currencies requested. userId={}", userId);
        return inventoryService.getUserCurrencies(Long.valueOf(userId));
    }
}