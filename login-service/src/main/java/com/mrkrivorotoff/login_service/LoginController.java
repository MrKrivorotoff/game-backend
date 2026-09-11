package com.mrkrivorotoff.login_service;

import com.mrkrivorotoff.login_service.proto.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.MediaType.APPLICATION_PROTOBUF_VALUE;

@Controller
@RequestMapping("auth")
public final class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = requireNonNull(loginService);
    }

    @ResponseBody
    @PostMapping(value = "login", consumes = APPLICATION_PROTOBUF_VALUE, produces = APPLICATION_PROTOBUF_VALUE)
    public ResponseEntity<Login.LoginResponse> login(@RequestBody Login.LoginRequest request) {
        var username = request.getUsername();
        log.info("login requested. username={}", username);
        return switch (loginService.login(username, request.getPassword())) {
            case AuthResult.Success success -> {
                var response = Login.LoginResponse.newBuilder()
                        .setToken(success.authToken())
                        .build();
                yield ResponseEntity.ok(response);
            }
            case AuthResult.InvalidRequest _ -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            case AuthResult.InvalidCredentials _ -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        };
    }
}