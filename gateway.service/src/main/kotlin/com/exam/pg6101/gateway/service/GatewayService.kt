package com.exam.pg6101.gateway.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class GatewayService

fun main(args: Array<String>) {
	runApplication<GatewayService>(*args)
}
