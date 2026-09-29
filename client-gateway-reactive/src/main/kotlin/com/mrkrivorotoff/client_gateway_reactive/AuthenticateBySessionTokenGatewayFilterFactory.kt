package com.mrkrivorotoff.client_gateway_reactive

import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange

data class Config(var headerName: String)

const val BEARER_PREFIX = "Bearer "

@Component
class AuthenticateBySessionTokenGatewayFilterFactory(redisTemplate: ReactiveStringRedisTemplate) :
    AbstractGatewayFilterFactory<Config>(Config::class.java) {

    private val redisValueOperations = redisTemplate.opsForValue()

    override fun apply(config: Config) = GatewayFilter { exchange, chain ->
        mono {
            val userId = getUserId(exchange)
                ?: return@mono unauthorized(exchange)
            val authenticatedExchange = exchange.mutate()
                .request { request ->
                    request.headers { headers ->
                        headers.set(config.headerName, userId)
                    }
                }
                .build()
            chain.filter(authenticatedExchange).awaitSingleOrNull()
        }
    }

    private suspend fun getUserId(exchange: ServerWebExchange): String? {
        val sessionId = exchange.request.headers
            .getFirst(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith(BEARER_PREFIX) }
            ?.substring(BEARER_PREFIX.length)
            ?.takeIf { it.isNotBlank() }
            ?: return null
        return redisValueOperations
            .get(sessionId)
            .awaitSingleOrNull()
    }

    private suspend fun unauthorized(exchange: ServerWebExchange) = exchange.response.run {
        statusCode = HttpStatus.UNAUTHORIZED
        headers.set(HttpHeaders.WWW_AUTHENTICATE, "Bearer")
        setComplete()
            .awaitSingleOrNull()
    }

    override fun shortcutFieldOrder() = listOf("headerName")
}