package com.orangefox.ripple.consolidation.policy.model

import com.orangefox.ripple.commons.models.AirlineCode
import com.orangefox.ripple.commons.models.AirportCode
import java.time.LocalDate
import java.util.*

class Policy(
  val id: PolicyId,
  val startDate: LocalDate,
  val endDate: LocalDate,
  val fareModifiers: Set<FareModifier> = emptySet(),
  val blackList: BlackList? = null,
  val whiteList: WhiteList? = null
)

class BlackList(
  val airlines: Set<AirlineCode>,
  val sectors: Set<Sector>,
  val sectorsOnAirlines: Set<Pair<AirlineCode, Sector>>
)

class WhiteList(
  val airlines: Set<AirlineCode>
)

data class Sector(val origin: AirportCode, val destination: AirportCode)

class PolicyId(val id: UUID)
