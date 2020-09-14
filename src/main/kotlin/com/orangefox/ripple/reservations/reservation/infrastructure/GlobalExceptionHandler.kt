package com.orangefox.ripple.reservations.reservation.infrastructure

import com.fasterxml.jackson.core.JsonParseException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.ResourceAccessException

@ControllerAdvice
open class GlobalExceptionHandler {

  @ExceptionHandler(JsonParseException::class)
  fun handleParseException(ex: JsonParseException): ResponseEntity<Any>{
    val response = mapOf(
      "error" to "Invalid Response from Server",
      "requestPayload" to ex.requestPayloadAsString,
      "location" to ex.location
    )

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
  }

  @ExceptionHandler(ResourceAccessException::class)
  fun handleGeneric(ex: ResourceAccessException): ResponseEntity<Any> {
    val cause = ex.cause
    if (cause is JsonParseException) {
      val response = mapOf(
        "error" to "Invalid Response from Server",
        "requestPayload" to cause.requestPayloadAsString,
        "location" to cause.location
      )

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
  }
}
