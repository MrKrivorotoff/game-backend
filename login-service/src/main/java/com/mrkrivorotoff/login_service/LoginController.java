package com.mrkrivorotoff.login_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
public final class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @ResponseBody
    @PostMapping("login")
    public String login() {
        log.info("login requested");
        return UUID.randomUUID().toString();
    }
}