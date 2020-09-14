package com.orangefox.ripple.reservations.farequote.infrastructure.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.math.BigDecimal
import java.util.*

data class FareQuote(
  val accessPoint: UUID,
  val contract: UUID,
  val contractor: UUID,
  val basePrice: BigDecimal,
  val taxes: BigDecimal,
  val adjustment: BigDecimal,
  val totalAmount: BigDecimal = basePrice + taxes + adjustment,
  val currencyCode: String,
  val segments: Set<SegmentRef>,
  val bookings: Set<Booking>
) {
  @Id var id: UUID? = null
}

data class SegmentRef(val segment: UUID, val connection: Boolean) {
  @Id var id: Long? = null
}

data class Booking(val passengerType: String, val count: Int, val details: Set<BookingInfo>) {
  @Id var id: Long? = null
}

data class BookingInfo(val bookingCode: String, val segmentRef: String, val fareBasis: String) {
  @Id var id: Long? = null
}

data class Segment( // TODO add segment status e.g HK etc
  val key: String,
  @Column("_GROUP") val group: Int,
  val origin: String,
  val destination: String,
  val departureTime: String,
  val arrivalTime: String,
  val carrier: String,
  val flightNumber: String,
  val equipment: String,
  val providerCode: String?
) {
  @Id var id: UUID? = null
}
