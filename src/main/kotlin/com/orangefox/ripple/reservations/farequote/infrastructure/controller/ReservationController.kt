package com.orangefox.ripple.reservations.farequote.infrastructure.controller.reservation

import com.orangefox.ripple.reservations.reservation.infrastructure.Traveler
import com.orangefox.ripple.reservations.reservation.infrastructure.UniversalRecord
import com.orangefox.ripple.reservations.reservation.infrastructure.gateway.CreateUniversalRecord
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@RepositoryRestController
open class ReservationController(val service: CreateUniversalRecord) {

  @PostMapping("fareQuotes/{id}/reservation")
  open fun makeReservation(@PathVariable("id") fareQuote: UUID, @RequestBody request: Request?): ResponseEntity<UniversalRecord> {
    val universalRecord = service.createUniversalRecord(fareQuote, request!!.travelers)
    return ResponseEntity.ok(universalRecord)
  }

}

class Request(val travelers: Set<Traveler>)
