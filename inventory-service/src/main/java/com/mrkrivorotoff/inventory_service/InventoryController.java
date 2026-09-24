package com.mrkrivorotoff.inventory_service;

import com.mrkrivorotoff.inventory_service.proto.Currencies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.MediaType.APPLICATION_PROTOBUF_VALUE;

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
    @GetMapping(value = "user_currencies", produces = APPLICATION_PROTOBUF_VALUE)
    public Currencies.GetUserCurrenciesResponse getUserCurrencies(@RequestHeader("X-User-Id") String userId) {
        log.info("GET inventory/user_currencies requested. userId={}", userId);
        return Currencies.GetUserCurrenciesResponse.newBuilder()
                .putAllUserCurrencies(inventoryService.getUserCurrencies(Long.valueOf(userId)))
                .build();
    }
}