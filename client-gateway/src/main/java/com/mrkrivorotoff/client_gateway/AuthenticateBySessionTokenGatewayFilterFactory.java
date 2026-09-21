package com.mrkrivorotoff.client_gateway;

import org.jspecify.annotations.NonNull;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Component
public final class AuthenticateBySessionTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticateBySessionTokenGatewayFilterFactory.Config> {
    private static final String BEARER_PREFIX = "Bearer ";

    private final ReactiveStringRedisTemplate redisTemplate;

    public AuthenticateBySessionTokenGatewayFilterFactory(ReactiveStringRedisTemplate redisTemplate) {
        super(Config.class);
        this.redisTemplate = requireNonNull(redisTemplate);
    }

    @Override
    public GatewayFilter apply(@NonNull Config config) {
        return (exchange, chain) -> {
            var authorization = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);
            if (authorization == null || !authorization.startsWith(BEARER_PREFIX))
                return unauthorized(exchange);
            var sessionId = authorization.substring(BEARER_PREFIX.length());
            if (sessionId.isBlank())
                return unauthorized(exchange);
            return redisTemplate.opsForValue()
                    .get(sessionId)
                    .defaultIfEmpty("")
                    .flatMap(userId -> {
                        if (userId.isEmpty())
                            return unauthorized(exchange);
                        var request = exchange.getRequest()
                                .mutate()
                                .headers(httpHeaders -> httpHeaders.set(config.getHeaderName(), userId))
                                .build();

                        return chain.filter(exchange.mutate().request(request).build());
                    });
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().set(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
        return response.setComplete();
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("headerName");
    }

    public static final class Config {
        private String headerName;

        public String getHeaderName() {
            return headerName;
        }

        public void setHeaderName(String headerName) {
            this.headerName = headerName;
        }
    }
}