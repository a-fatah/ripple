package com.orangefox.ripple.reservations.farequote.infrastructure.gateway

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import com.orangefox.ripple.reservations.farequote.infrastructure.gateway.Request as ConsumerRequest

interface GatewayInterface {
  fun searchFares(request: ConsumerRequest): Response
}

@Component
open class Connector(@Value("\${endpoints.air.search}") val searchEndpoint: String, val restTemplate: RestTemplate): GatewayInterface {

  override fun searchFares(request: ConsumerRequest): Response {
    val headers = HttpHeaders()
    headers.set("Username", request.hap.username)
    headers.set("Password", request.hap.password)
    headers.set("Branch", request.hap.branch)
    val response = restTemplate.postForEntity(searchEndpoint, HttpEntity(request, headers), Response::class.java)
    return response.body
  }

  companion object logging: KLogging()
}
