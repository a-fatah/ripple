package com.orangefox.ripple.reservations.farequote.infrastructure.gateway

import com.orangefox.ripple.commons.models.Leg
import com.orangefox.ripple.commons.models.Passenger
import com.orangefox.ripple.consolidation.agency.model.HostAccessProfile

class Request(
  val hap: HostAccessProfile,
  val legs: List<Leg>,
  val passengers: List<Passenger>
)
