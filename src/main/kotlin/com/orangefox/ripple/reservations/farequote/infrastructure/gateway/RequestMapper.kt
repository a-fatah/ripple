package com.orangefox.ripple.reservations.farequote.infrastructure.gateway

import com.orangefox.ripple.commons.models.Leg
import com.orangefox.ripple.commons.models.Passenger
import com.orangefox.ripple.consolidation.agency.model.HostAccessProfile
import org.springframework.stereotype.Component

@Component
class RequestMapper {
  fun mapRequest(request: com.orangefox.ripple.reservations.farequote.infrastructure.controller.search.Request, hap: HostAccessProfile): Request {
    return Request(
      hap = hap,
      legs = request.legs.map { Leg(it.origin, it.destination, it.departureDate) },
      passengers = request.passengers.map { Passenger(type = it.type, count = it.count) }
    )
  }
}
