package com.orangefox.ripple.consolidation.policy.infrastructure

import com.orangefox.ripple.commons.models.AirlineCode
import com.orangefox.ripple.commons.models.AirportCode
import com.orangefox.ripple.consolidation.contract.infrastructure.CalculationStrategy.*
import com.orangefox.ripple.consolidation.policy.model.*
import com.orangefox.ripple.consolidation.policy.model.Commission
import com.orangefox.ripple.consolidation.policy.model.Discount
import com.orangefox.ripple.consolidation.policy.model.Policy
import java.util.stream.Collectors
import java.util.stream.Stream
import com.orangefox.ripple.consolidation.policy.infrastructure.Policy as PolicyEntity


object DomainMapper {

  fun mapToDomain(policy: PolicyEntity): Policy {
    val blackList = BlackList(
      airlines = policy.blockedAirlines.map { AirlineCode(it.airline) }.toSet(),
      sectors = policy.blockedSectors.map {
        Sector(origin = AirportCode(it.origin), destination = AirportCode(it.dest))
      }.toSet(),
      sectorsOnAirlines = policy.blockedSectorsOnAirlines.map {
        AirlineCode(it.airline) to Sector(origin = AirportCode(it.origin), destination = AirportCode(it.dest))
      }.toSet()
    )

    val whiteList = WhiteList(
      airlines = policy.allowedAirlines.map { AirlineCode(it.airline) }.toSet()
    )

    val commissions = policy.commissions.map {
      val strategy = when (it.strategy) {
        FLAT -> FlatAdjustment(it.name, it.value)
        PERCENTAGE_OF_BASE_FARE -> PercentageOfBaseFare(it.name, it.value)
        PERCENTAGE_OF_TOTAL_AMOUNT -> PercentageOfTotal(it.name, it.value)
        PERCENTAGE_OF_TOTAL_TAXES -> PercentageOfTotal(it.name, it.value)
      }
      Commission(strategy)
    }.toSet()

    val discounts = policy.discounts.map {
      val strategy = when(it.strategy) {
        FLAT -> FlatAdjustment(it.name, it.value)
        PERCENTAGE_OF_BASE_FARE -> PercentageOfBaseFare(it.name, it.value)
        PERCENTAGE_OF_TOTAL_AMOUNT -> PercentageOfTotal(it.name, it.value)
        PERCENTAGE_OF_TOTAL_TAXES -> PercentageOfTaxes(it.name, it.value)
      }
      Discount(strategy)
    }

    val fareModifiers = Stream.concat(commissions.stream(), discounts.stream()).collect(Collectors.toSet())

    return Policy(
      id = PolicyId(policy.id!!),
      startDate = policy.startDate,
      endDate = policy.endDate,
      fareModifiers = fareModifiers,
      blackList = blackList,
      whiteList = whiteList
    )
  }
}
