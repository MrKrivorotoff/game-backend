package com.mrkrivorotoff.auth_service;

import com.mrkrivorotoff.auth_service.proto.Login;
import com.mrkrivorotoff.auth_service.proto.Register;
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
public final class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final SessionService sessionService;

    @Autowired
    public AuthController(AuthService authService, SessionService sessionService) {
        this.authService = requireNonNull(authService);
        this.sessionService = requireNonNull(sessionService);
    }

    @ResponseBody
    @PostMapping(value = "login", consumes = APPLICATION_PROTOBUF_VALUE, produces = APPLICATION_PROTOBUF_VALUE)
    public ResponseEntity<Login.LoginResponse> login(@RequestBody Login.LoginRequest request) {
        var username = request.getUsername();
        log.info("POST auth/login requested. username={}", username);
        return switch (authService.login(username, request.getPassword())) {
            case LoginResult.Success success -> {
                var sessionId = sessionService.createUserSession(success.userId());
                var response = Login.LoginResponse.newBuilder()
                        .setSessionId(sessionId)
                        .build();
                yield ResponseEntity.ok(response);
            }
            case LoginResult.InvalidLoginData _ -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            case LoginResult.InvalidCredentials _ -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        };
    }

    @PostMapping(value = "register", consumes = APPLICATION_PROTOBUF_VALUE)
    public ResponseEntity<Void> register(@RequestBody Register.RegisterRequest request) {
        var username = request.getUsername();
        log.info("POST auth/register requested. username={}", username);
        return switch (authService.registerNewUser(username, request.getPassword())) {
            case SUCCESS -> ResponseEntity.status(HttpStatus.CREATED).build();
            case INVALID_REGISTRATION_DATA -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            case CONFLICT -> ResponseEntity.status(HttpStatus.CONFLICT).build();
        };
    }
}