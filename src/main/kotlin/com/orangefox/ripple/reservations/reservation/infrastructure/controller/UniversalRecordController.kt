package com.orangefox.ripple.reservations.reservation.infrastructure.controller

import com.orangefox.ripple.reservations.reservation.infrastructure.Reservation
import com.orangefox.ripple.reservations.reservation.infrastructure.ReservationRepository
import com.orangefox.ripple.reservations.reservation.infrastructure.UniversalRecord
import com.orangefox.ripple.reservations.reservation.infrastructure.UniversalRecordRepository
import com.orangefox.ripple.reservations.reservation.infrastructure.gateway.CancelUniversalRecord
import com.orangefox.ripple.reservations.reservation.infrastructure.resource.UniversalRecordAssembler
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*


@RepositoryRestController
open class UniversalRecordController(
  private val canceller: CancelUniversalRecord,
  private val recordRepository: UniversalRecordRepository,
  private val entityLinks: RepositoryEntityLinks,
  private val reservationRepository: ReservationRepository) {

  @PostMapping("universalRecords/{id}/cancel")
  open @ResponseBody fun cancelItinerary(@PathVariable("id") recordId: UUID): EntityModel<UniversalRecord> {
    val universalRecord = canceller.cancelUniversalRecord(recordId)
    val model = UniversalRecordAssembler().toModel(universalRecord)
    model.add(entityLinks.linkToItemResource(UniversalRecord::class.java, universalRecord.id).withSelfRel())
    return model
  }

  @GetMapping("universalRecords/{id}/reservations")
  open @ResponseBody fun listReservations(@PathVariable("id") recordId: UUID): CollectionModel<Reservation> {
    val universalRecord = recordRepository.findById(recordId)
    val reservations = universalRecord.map {
      val reservations = it.reservations.map { ref -> reservationRepository.findById(ref.reservation) }
      check(reservations.all { it.isPresent }) {
        "Some reservations could not be found in database"
      }
      reservations.map { it.get() }
    }.orElseThrow { IllegalStateException("Universal Record ${recordId} could not be found") }

    return CollectionModel.of(reservations)
  }

}
