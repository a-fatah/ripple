package com.orangefox.ripple.reservations.reservation.infrastructure.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

open class UAPIGatewayErrorHandler(private val objectMapper: ObjectMapper): ResponseErrorHandler {

  override fun hasError(response: ClientHttpResponse): Boolean {
    val statusCode = response.statusCode
    return with(statusCode) {
      this.is4xxClientError || this.is5xxServerError
    }
  }

  override fun handleError(response: ClientHttpResponse) {
    val statusCode = response.statusCode
    val errorResponse = BufferedReader(InputStreamReader(response.body))
      .lines()
      .collect(Collectors.joining("\n"))

    when(statusCode) {
      UNPROCESSABLE_ENTITY -> {
        val error = objectMapper.readValue(errorResponse, ErrorDetails::class.java)
        throw UAPIException(
          error.code,
          error.traceId,
          error.transactionId,
          error.description
        )
      }
      INTERNAL_SERVER_ERROR -> {
        val map = objectMapper.readValue(errorResponse, Map::class.java)
        val message = map.get("message")?.toString() ?: "No error message received from Service"
        throw ServiceFailure(message)
      }
    }
  }

  companion object: KLogging()
}

class ServiceFailure(msg: String): RuntimeException(msg)
class UAPIException(val code: String?, val traceId: String?, val transactionId: String, description: String): IllegalStateException(description)

data class ErrorDetails(val traceId: String?, val code: String, val description: String, val transactionId: String)
