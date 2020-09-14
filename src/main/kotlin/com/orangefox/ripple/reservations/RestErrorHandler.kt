package com.orangefox.ripple.reservations

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
open class RestErrorHandler: ResponseEntityExceptionHandler() {

  @ExceptionHandler(IllegalStateException::class)
  fun handle(ex: IllegalStateException): ResponseEntity<ErrorResponse> {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse(ex.message!!))
  }

}

data class ErrorResponse(val message: String)
