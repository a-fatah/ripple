package com.orangefox.ripple.reservations.reservation.infrastructure.gateway

import com.orangefox.ripple.app.events.publisher.DomainEvents
import com.orangefox.ripple.consolidation.agency.model.AccessPointId
import com.orangefox.ripple.consolidation.agency.model.Agencies
import com.orangefox.ripple.consolidation.agency.model.HostAccessProfile
import com.orangefox.ripple.reservations.reservation.infrastructure.ReservationRepository
import com.orangefox.ripple.reservations.reservation.infrastructure.UniversalRecord
import com.orangefox.ripple.reservations.reservation.infrastructure.UniversalRecordRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*

@Component
class CancelUniversalRecordImpl(
  private val domainEvents: DomainEvents,
  private val universalRecords: UniversalRecordRepository,
  private val reservationRepository: ReservationRepository,
  private val agencies: Agencies,
  private val restTemplate: RestTemplate): CancelUniversalRecord {

  @Value("\${endpoints.air.cancel}")
  private lateinit var cancelEndpoint: String

  override fun cancelUniversalRecord(universalRecord: UUID): UniversalRecord {

    val universalRecord = findUniversalRecord(universalRecord)
    val credentials = findCredentials(universalRecord.accessPoint)

    val request = Request(credentials, universalRecord.locatorCode)
    val response = restTemplate.postForEntity(cancelEndpoint, request, Response::class.java)

    val statusList = response.body.statusList

    val reservations = universalRecord.reservations.map { reservationRepository.findById(it.reservation) }
    check(reservations.all { it.isPresent }) {
      "Not all reservations were found in database"
    }
    val updatedReservations = statusList.map { status ->
      val reservation = reservations.map { it.get() }.find {
       it.providerCode == status.providerCode && it.providerLocatorCode == status.locatorCode
      } ?: error("Reservation status could not be mapped to reservation in database")
      val cancelledReservation = reservation.copy(cancelled = status.cancelled)
      cancelledReservation.id = reservation.id
      cancelledReservation
    }.toSet()

    reservationRepository.saveAll(updatedReservations)

    return universalRecords.save(universalRecord.copy(cancelled = true))
  }

  private fun findUniversalRecord(id: UUID): UniversalRecord {
    return universalRecords.findById(id).orElseThrow { IllegalStateException("Could not find Reservation") }
  }

  private fun findCredentials(id: UUID): HostAccessProfile {
    val accessPoint = agencies.findAccessPoint(AccessPointId(id))
      ?: throw IllegalStateException("Could not find accesspoint which was used for creating Universal Record")
    return accessPoint.credentials as HostAccessProfile
  }
}

class Request(
  val hap: HostAccessProfile,
  val locatorCode: String
)

open class Response(val statusList: List<ReservationStatus>)

class ReservationStatus(
  val locatorCode: String,
  val providerCode: String,
  val createdDate: String,
  val modifiedDate: String,
  val cancelled: Boolean
)
