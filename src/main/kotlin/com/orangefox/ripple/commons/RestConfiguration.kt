package com.orangefox.ripple.commons

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.orangefox.ripple.reservations.reservation.infrastructure.gateway.UAPIGatewayErrorHandler
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate

@Configuration
open class RestConfiguration {

  @Bean
  open fun restTemplate(): RestTemplate {
    return RestTemplateBuilder().errorHandler(adapterErrorHandler()).build()
  }

  @Bean
  open fun jacksonMapper(): ObjectMapper {
    return jacksonObjectMapper()
  }

  @Bean
  open fun adapterErrorHandler(): ResponseErrorHandler {
    return UAPIGatewayErrorHandler(jacksonMapper())
  }

}
