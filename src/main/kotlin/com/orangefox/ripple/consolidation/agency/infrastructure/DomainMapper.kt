package com.orangefox.ripple.consolidation.agency.infrastructure

import com.orangefox.ripple.commons.models.CityCode
import com.orangefox.ripple.commons.models.CountryCode
import com.orangefox.ripple.commons.models.Email
import com.orangefox.ripple.commons.models.PhoneNumber
import com.orangefox.ripple.consolidation.agency.model.*
import com.orangefox.ripple.consolidation.agency.model.AccessPoint
import com.orangefox.ripple.consolidation.agency.model.Address
import com.orangefox.ripple.consolidation.agency.model.Credentials
import com.orangefox.ripple.consolidation.policy.infrastructure.DomainMapper
import com.orangefox.ripple.consolidation.policy.infrastructure.PolicyRepository
import org.springframework.stereotype.Component
import com.orangefox.ripple.consolidation.agency.infrastructure.AccessPoint as AccessPointEntity
import com.orangefox.ripple.consolidation.agency.infrastructure.Credentials as CredentialsEntity
import com.orangefox.ripple.consolidation.agency.model.Agency as AgencyModel

@Component("agencyDomainMapper")
class DomainMapper(private val policies: PolicyRepository) {

  fun mapToDomain(entity: Agency): AgencyModel {
    return AgencyModel(
      id = AgencyId(entity.id
        ?: throw IllegalArgumentException("Cannot convert transient Agency entity to domain")),
      name = AgencyName(entity.name),
      contact = Contact(
        email = Email(entity.email),
        phone = PhoneNumber(entity.phone),
        address = Address(
          entity.address.street,
          CityCode(entity.address.city),
          CountryCode(entity.address.country)
        )
      ),
      accessPoints = entity.accessPoints.map { mapToDomain(it) },
      policies = entity.policies.map {
        val policy = policies.findById(it.policy).orElseThrow { IllegalStateException("Policy not found") }
        DomainMapper.mapToDomain(policy)
      }
    )
  }

  fun mapToDomain(entity: AccessPointEntity): AccessPoint =

    when(entity.provider) {
      Provider.GALILEO -> TravelportUAPI(
        id = AccessPointId(entity.id!!),
        name = AccessPointName(entity.name),
        credentials = entity.credentials.let {
          HostAccessProfile(
            username = it.username,
            password = it.password,
            branch = it.targetBranch
          )
        }
      )

      else -> AccessPoint(
        id = AccessPointId(entity.id!!),
        name = AccessPointName(entity.name),
        credentials = mapCredentials(entity.credentials)
      )

  }

  private fun mapCredentials(credentials: CredentialsEntity): Credentials =
    when(credentials.type) {
      CredentialType.HAP -> HostAccessProfile(
        username = credentials.username,
        password = credentials.password,
        branch = credentials.targetBranch
      )
      CredentialType.USERNAME_PASSWORD -> UsernamePassword(credentials.username, credentials.password)
    }
}
