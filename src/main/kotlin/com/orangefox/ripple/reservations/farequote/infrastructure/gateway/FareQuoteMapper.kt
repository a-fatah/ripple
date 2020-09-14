package com.orangefox.ripple.reservations.farequote.infrastructure.gateway

import com.orangefox.ripple.consolidation.agency.model.TravelportUAPI
import com.orangefox.ripple.reservations.contractor.model.ActiveContract
import com.orangefox.ripple.reservations.contractor.model.Contractor
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.Booking
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.BookingInfo
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.Segment
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.SegmentRef
import java.math.BigDecimal
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.FareQuote as FareQuoteEntity

object FareQuoteMapper {

  fun mapToEntity(model: FareQuote, segments: List<Segment>, contract: ActiveContract, contractor: Contractor, api: TravelportUAPI): FareQuoteEntity {
    val segmentKeyMap = segments.map { it.key to it.id!! }.toMap()
    val segmentRefs = model.segments.map { ref ->
      val id = segmentKeyMap.get(ref.key) ?: error("could not find ID for segment with key ${ref.key}")
      SegmentRef(id, ref.connection)
    }.toSet()
    return FareQuoteEntity(
      accessPoint = api.accessPointId,
      contractor = contractor.agencyId,
      contract = contract.contractId,
      basePrice = model.fare.baseFare.substring(3).toBigDecimal(),
      taxes = model.fare.taxes.substring(3).toBigDecimal(),
      totalAmount = model.fare.totalAmount.substring(3).toBigDecimal(),
      adjustment = BigDecimal.ZERO,
      currencyCode = model.fare.totalAmount.substring(0, 2),
      segments = segmentRefs,
      bookings = model.bookings.map {
        Booking(
          it.passengerType,
          it.count,
          it.details.map { BookingInfo(it.bookingCode, it.segmentRef, it.fareBasis) }.toSet())
      }.toSet()
    )
  }
}
