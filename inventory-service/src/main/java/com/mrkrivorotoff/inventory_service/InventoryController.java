package com.mrkrivorotoff.inventory_service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("inventory")
public final class InventoryController {
    @ResponseBody
    @GetMapping("user_currencies")
    public Map<String, Long> getUserCurrencies() {
        return Map.of("Gold", 1000L);
    }
}