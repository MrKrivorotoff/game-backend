package com.mrkrivorotoff.client_gateway;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static com.mrkrivorotoff.client_gateway.GatewayRoutesConfiguration.USER_ID_HEADER_NAME;

@Component
public final class AuthenticateBySessionTokenFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    private static final String BEARER_PREFIX = "Bearer ";

    private final ValueOperations<String, String> redisValueOperations;

    @Autowired
    public AuthenticateBySessionTokenFilter(StringRedisTemplate redisTemplate) {
        this.redisValueOperations = redisTemplate.opsForValue();
    }

    @Override
    public ServerResponse filter(@NonNull ServerRequest request, @NonNull HandlerFunction<ServerResponse> next) throws Exception {
        var authorization = request.headers()
                .firstHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            var sessionId = authorization.substring(BEARER_PREFIX.length());
            if (!sessionId.isBlank()) {
                var userId = redisValueOperations.get(sessionId);
                if (userId != null) {
                    var authenticatedRequest = ServerRequest.from(request)
                            .header(USER_ID_HEADER_NAME, userId)
                            .build();
                    return next.handle(authenticatedRequest);
                }
            }
        }
        return ServerResponse
                .status(HttpStatus.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, "Bearer")
                .build();
    }
}