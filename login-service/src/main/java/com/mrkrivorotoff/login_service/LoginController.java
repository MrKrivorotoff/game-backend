package com.mrkrivorotoff.login_service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
public final class LoginController {
    @ResponseBody
    @PostMapping("login")
    public String login() {
        return UUID.randomUUID().toString();
    }
}