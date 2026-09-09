package com.mrkrivorotoff.login_service;

import com.mrkrivorotoff.login_service.proto.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_PROTOBUF_VALUE;

@Controller
public final class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @ResponseBody
    @PostMapping(value = "login", consumes = APPLICATION_PROTOBUF_VALUE, produces = APPLICATION_PROTOBUF_VALUE)
    public Login.LoginResponse login(Login.LoginRequest request) {
        log.info("login requested. login={}", request.getLogin());
        return Login.LoginResponse.newBuilder()
                .setToken(UUID.randomUUID().toString())
                .build();
    }
}