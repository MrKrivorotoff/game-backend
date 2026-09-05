package com.mrkrivorotoff.inventory_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("inventory")
public final class InventoryController {
    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

    @ResponseBody
    @GetMapping("user_currencies")
    public Map<String, Long> getUserCurrencies() {
        log.info("inventory.getUserCurrencies requested");
        return Map.of("Gold", 1000L);
    }
}