package com.orangefox.ripple.consolidation.agency.application

import com.orangefox.ripple.consolidation.agency.infrastructure.AccessPointRepository
import com.orangefox.ripple.consolidation.agency.infrastructure.AgencyRepository
import com.orangefox.ripple.consolidation.agency.infrastructure.DomainMapper
import com.orangefox.ripple.consolidation.agency.model.*
import com.orangefox.ripple.consolidation.contract.model.Consolidator
import com.orangefox.ripple.consolidation.contract.model.Contractor
import com.orangefox.ripple.consolidation.contract.model.NumberOfHaps
import java.util.*


open class AgencyRepository(
  private val agencyRepository: AgencyRepository,
  private val accessPointRepository: AccessPointRepository,
  private val domainMapper: DomainMapper): Agencies {

  override fun findAgency(agency: AgencyId): Agency? {
    return agencyRepository.findById(agency.id).map { domainMapper.mapToDomain(it) }.orElse(null)
  }

  override fun findConsolidator(agency: AgencyId): Consolidator? {
    val consolidator = findAgency(agency)

    consolidator?.let {
      val haps = it.accessPoints.size
      return Consolidator(consolidator.id, NumberOfHaps(haps))
    } ?: throw IllegalStateException("Consolidator not found with id ${agency.id}")
  }

  override fun findContractor(agency: AgencyId): Contractor? {
    val contractor = findAgency(agency)
    return contractor?.let {
      Contractor(agency)
    } ?: throw IllegalStateException("Contractor not found with id ${agency.id}")
  }

  override fun findAccessPoint(accessPoint: AccessPointId): AccessPoint? {
    return accessPointRepository.findById(accessPoint.id).map { domainMapper.mapToDomain(it) }.orElse(null)
  }

}

// TODO remove exception handling from repository and let the clients decide what to do when data not found

fun <T : Any> Optional<T>.toNullable(): T? {
  return if (this.isPresent) {
    this.get()
  } else {
    null
  }
}
