package com.mrkrivorotoff.client_gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static java.util.Objects.requireNonNull;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.removeRequestHeader;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayRoutesConfiguration {
    public static final String USER_ID_HEADER_NAME = "X-User-Id";

    private final AuthenticateBySessionTokenFilter authenticateBySessionToken;

    @Autowired
    public GatewayRoutesConfiguration(AuthenticateBySessionTokenFilter authenticateBySessionToken) {
        this.authenticateBySessionToken = requireNonNull(authenticateBySessionToken);
    }

    @Bean
    public RouterFunction<ServerResponse> authServiceRoute() {
        return route("auth-service")
                .route(path("/auth/**"), http())
                .before(uri("http://auth-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute() {
        return route("inventory-service")
                .route(path("/inventory/**"), http())
                .before(removeRequestHeader(USER_ID_HEADER_NAME))
                .filter(authenticateBySessionToken)
                .before(removeRequestHeader(HttpHeaders.AUTHORIZATION))
                .before(uri("http://inventory-service:8080"))
                .build();
    }
}