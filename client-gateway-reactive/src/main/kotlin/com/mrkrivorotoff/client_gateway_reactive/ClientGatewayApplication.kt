package com.mrkrivorotoff.client_gateway_reactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClientGatewayApplication

fun main(args: Array<String>) {
    runApplication<ClientGatewayApplication>(*args)
}