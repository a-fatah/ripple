package com.orangefox.ripple.reservations.contractor.infrastructure

import com.orangefox.ripple.consolidation.agency.model.AccessPointId
import com.orangefox.ripple.consolidation.agency.model.AgencyId
import com.orangefox.ripple.consolidation.agency.model.AgencyName
import com.orangefox.ripple.consolidation.contract.model.ContractId
import com.orangefox.ripple.consolidation.policy.model.PolicyId
import com.orangefox.ripple.reservations.contractor.model.*
import com.orangefox.ripple.reservations.contractor.model.Contract
import com.orangefox.ripple.reservations.contractor.model.Contractor
import com.orangefox.ripple.reservations.contractor.infrastructure.Contract as ContractEntity
import com.orangefox.ripple.reservations.contractor.infrastructure.Contractor as ContractorEntity

class ContractMapper {

  fun toDomain(entity: ContractEntity): Contract {

    return when(entity.state) {

      ContractState.ACTIVE -> ActiveContract(
        id = ContractId(entity.id ?: throw IllegalArgumentException("Cannot map transient entity to ActiveContract domain object")),
        consolidator = AgencyId(entity.consolidator),
        accessPoints = entity.accessPoints.map { AccessPointId(it.ref) },
        policies = entity.policies.map { PolicyId(it.ref) },
        balance = Balance(entity.balance)
      )

      else -> InactiveContract(
        id = ContractId(entity.id ?: throw IllegalArgumentException("Cannot map transient entity to SuspendedContract domain object")),
        consolidator = AgencyId(entity.consolidator),
        balance = Balance(entity.balance)
      )

//      ContractState.PENDING -> PendingContract(
//        ContractId(entity.id!!),
//        AgencyId(entity.consolidator),
//        balance = Balance(entity.balance)
//      )
    }
  }

  fun toEntity(contract: Contract): ContractEntity {
    return ContractEntity(
      id = contract.id.id,
      consolidator = contract.consolidator.id,
      balance = contract.balance.amount,
      state = when(contract) {
        is ActiveContract -> ContractState.ACTIVE
//        is InactiveContract -> ContractState.SUSPENDED
        else -> ContractState.INACTIVE
      }
    )
  }
}

class ContractorMapper {
  fun toDomain(entity: ContractorEntity): Contractor {
    return Contractor(
      id = AgencyId(entity.id!!),
      name = AgencyName(entity.name),
      contracts = entity.contracts.map { ContractMapper().toDomain(it) }.toSet(),
      policies = entity.policies.map { PolicyId(it.ref) }
    )
  }

  fun toEntity(contractor: Contractor): ContractorEntity {
    return ContractorEntity(
      id = contractor.id.id,
      name = contractor.name.name,
      contracts = contractor.contracts.map { ContractMapper().toEntity(it) }.toSet()
    ).isNew(true)
  }
}
