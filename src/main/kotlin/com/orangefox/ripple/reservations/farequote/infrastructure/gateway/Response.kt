package com.orangefox.ripple.reservations.farequote.infrastructure.gateway

import com.orangefox.ripple.commons.models.Message
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.Segment

data class Response(val messages: List<Message>, val segments: List<Segment>, val fareQuotes: List<FareQuote>)

data class FareQuote(
  val fare: Fare,
  val segments: Set<SegmentRef>,
  val bookings: Set<Booking>
)

data class SegmentRef(val key: String, val connection: Boolean)
data class Booking(val passengerType: String, val count: Int, val details: List<BookingInfo>)
data class BookingInfo(val bookingCode: String, val segmentRef: String, val fareBasis: String)

data class Fare(
  val baseFare: String,
  val taxes: String,
  val totalAmount: String
)
