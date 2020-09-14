package com.orangefox.ripple.reservations.reservation.infrastructure.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
open class TicketingController {

  @PostMapping("ticket/{id}/refund")
  open fun refundTicket() {

  }

  @PostMapping("ticket/{id}/exchange")
  open fun exchangeTicket() {

  }

}
