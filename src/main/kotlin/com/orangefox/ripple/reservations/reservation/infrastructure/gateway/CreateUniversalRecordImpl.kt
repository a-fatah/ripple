package com.orangefox.ripple.reservations.reservation.infrastructure.gateway.create

import com.orangefox.ripple.app.events.publisher.DomainEvents
import com.orangefox.ripple.consolidation.agency.model.AccessPointId
import com.orangefox.ripple.consolidation.agency.model.Agencies
import com.orangefox.ripple.consolidation.agency.model.HostAccessProfile
import com.orangefox.ripple.consolidation.agency.model.TravelportUAPI
import com.orangefox.ripple.consolidation.contract.model.*
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.*
import com.orangefox.ripple.reservations.reservation.infrastructure.*
import com.orangefox.ripple.reservations.reservation.infrastructure.SegmentRef
import com.orangefox.ripple.reservations.reservation.infrastructure.gateway.CreateUniversalRecord
import com.orangefox.ripple.reservations.reservation.model.UniversalRecordCreated
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*
import com.orangefox.ripple.reservations.reservation.infrastructure.Reservation as ReservationEntity
import com.orangefox.ripple.reservations.reservation.infrastructure.UniversalRecord as UniversalRecordEntity

@Component
class CreateUniversalRecordImpl(
  private val fareQuotes: FareQuoteRepository,
  private val segmentsRepository: SegmentsRepository,
  private val reservationRepository: ReservationRepository,
  private val universalRecords: UniversalRecordRepository,
  private val travelerRepository: TravelerRepository,
  private val contracts: Contracts,
  private val agencies: Agencies,
  private val restTemplate: RestTemplate,
  private val domainEvents: DomainEvents): CreateUniversalRecord {

  @Value("\${endpoints.air.book}")
  private lateinit var endpoint: String

  override fun createUniversalRecord(fareQuote: UUID, travelers: Set<Traveler>): UniversalRecordEntity {
    val quote = getFareQuote(fareQuote)
    val contract = getContract(fareQuote)

    check(contract is ActiveContract) {
      "Contract is not active"
    }
    val transactionEvents = contract.performTransaction(quote.totalAmount)

    val failures = transactionEvents.filterIsInstance(TransactionFailed::class.java)

    if (failures.isNotEmpty()) {
      throw IllegalStateException(failures.first().reason)
    } else {
      val uapi = getAccessPoint(quote)
      val segments = quote.segments.map { segmentsRepository.findById(it.segment) }
      check(segments.all { it.isPresent }) {
        "Some of the Fare Quote segments could not be found in the database"
      }

      val request = Request(
        hap = uapi.credentials,
        travelers = travelers,
        segments = segments.map { it.get() }.toSet(),
        bookings = quote.bookings
      )
      val response = restTemplate.postForEntity(endpoint, request, Response::class.java)
      val universalRecord = response.body.universalRecord

      val reservationSegments = universalRecord.reservations.map {
        it.locatorCode to segmentsRepository.saveAll(it.segments)
      }.toMap()

      val travelers = travelerRepository.saveAll(universalRecord.travelers)

      val reservations = reservationRepository.saveAll(
        universalRecord.reservations.map { reservation ->
          val segments = reservationSegments.get(reservation.locatorCode)
            ?: throw IllegalStateException("Could not find segments")

          ReservationEntity(
            locatorCode = reservation.locatorCode,
            providerCode = reservation.providerCode,
            providerLocatorCode = reservation.providerLocatorCode,
            supplierCode = reservation.supplierCode,
            supplierLocatorCode = reservation.supplierLocatorCode,
            createdDate = reservation.createdDate,
            modifiedDate = reservation.modifiedDate,
            cancelled = reservation.cancelled,
            segments =  segments.map { SegmentRef(it.id!!) }.toSet()
          )
        }.toSet()
      )

      val entity = UniversalRecordEntity(
        locatorCode = universalRecord.locatorCode,
        accessPoint = uapi.accessPointId,
        contract = contract.contractId,
        travelers = travelers.map { TravelerRef(it.id!!) }.toSet(),
        reservations = reservations.map { ReservationRef(it.id!!) }.toSet(),
        status = universalRecord.status,
        version = universalRecord.version
      )

      val saved = universalRecords.save(entity)
      domainEvents.publish(UniversalRecordCreated(saved))
      return saved
    }
  }

  fun getFareQuote(id: UUID): FareQuote {
    return fareQuotes.findById(id).orElseThrow {
      IllegalArgumentException("Could not find FareQuote with id ${id}")
    }
  }

  private fun getAccessPoint(fareQuote: FareQuote): TravelportUAPI {
    val accessPoint = agencies.findAccessPoint(AccessPointId(fareQuote.accessPoint))
      ?: throw IllegalStateException("Could not find accesspoint which was used for requesting farequote")

    return accessPoint as TravelportUAPI
  }

  private fun getContract(fareQuote: UUID): Contract {

    val fareQuote = getFareQuote(fareQuote)
    val contract = contracts.findById(ContractId(fareQuote.contract))
      ?: throw IllegalStateException("Contract not found while making reservation")
    return contract
  }

}

class Request(
  val hap: HostAccessProfile,
  val travelers: Set<Traveler>,
  val segments: Set<Segment>,
  val bookings: Set<Booking>
)

class Response(val universalRecord: UniversalRecord)

open class UniversalRecord(
  val locatorCode: String,
  val version: String,
  val travelers: Set<Traveler>,
  val reservations: Set<Reservation>,
  val status: String
)

class Reservation(
  val locatorCode: String,
  val providerCode: String,
  val providerLocatorCode: String,
  val supplierCode: String,
  val supplierLocatorCode: String,
  val createdDate: String,
  val modifiedDate: String,
  val cancelled: Boolean = false,
  val segments: Set<Segment>
)
