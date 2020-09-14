package com.orangefox.ripple.reservations.farequote.model

import com.orangefox.ripple.commons.models.Airline
import com.orangefox.ripple.commons.models.AirlineCode
import com.orangefox.ripple.commons.models.AirportCode
import com.orangefox.ripple.reservations.contractor.model.Contract
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

typealias Decimal = BigDecimal

data class FareQuote(
  val id: QuoteId,
  val contract: Contract,
  val flights: List<Flight>,
  val fare: Fare,
  val stops: NumberOfStops
)

data class QuoteId(val id: UUID)

data class Flight(
  val flightNumber: FlightNumber,
  val origin: AirportCode,
  val destination: AirportCode,
  val departure: DepartureTime,
  val arrival: ArrivalTime,
  val airline: Airline,
  val operatingCarrier: AirlineCode,
  val distance: Distance,
  val numberOfStops: NumberOfStops,
  val cabinClass: CabinClass,
  val bookingClass: BookingClass,
  val segment: Segment,
  val equipment: Aircraft,
  val flightTime: FlightTime
)

data class DepartureTime(val dateTime: LocalDateTime)
data class ArrivalTime(val dateTime: LocalDateTime)
data class Segment(val ref: String)

data class NumberOfStops(val stops: Int)

data class Aircraft(val equipment: String)

data class FlightNumber(val number: String)
data class FlightTime(val time: ZonedDateTime)
data class Distance(val distance: Double, val unit: String)
data class CabinClass(val cabinClass: String)
data class BookingClass(val code: String)

data class Fare(
  val baseAmount: Decimal,
  val taxes: Decimal,
  val adjustment: Decimal,
  val currency: Currency
) {
  val totalAmount: Decimal = baseAmount + taxes + adjustment
}

val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

data class DepartureDate(val date: LocalDate) {
  override fun toString(): String {
    return date.format(formatter)
  }

  companion object {
    fun fromString(date: String): DepartureDate {
      return DepartureDate(LocalDate.parse(date))
    }
  }
}

data class ReturnDate(val date: LocalDate) {
  override fun toString(): String {
    return date.format(formatter)
  }

  companion object {
    fun fromString(date: String): ReturnDate {
      return ReturnDate(LocalDate.parse(date))
    }
  }
}

open class Passenger(val age: Int? = null, val dob: String? = null)
class Adult(): Passenger()
class Child(age: Int): Passenger(age)
class Infant(age: Int, dob: String): Passenger(age, dob)
