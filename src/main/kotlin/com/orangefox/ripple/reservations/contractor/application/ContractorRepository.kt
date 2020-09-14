package com.orangefox.ripple.reservations.contractor.application

import com.orangefox.ripple.consolidation.agency.model.AgencyId
import com.orangefox.ripple.consolidation.contract.model.ContractActivated
import com.orangefox.ripple.consolidation.contract.model.ContractCreated
import com.orangefox.ripple.consolidation.contract.model.ContractId
import com.orangefox.ripple.consolidation.contract.model.ContractSuspended
import com.orangefox.ripple.reservations.contractor.infrastructure.*
import com.orangefox.ripple.reservations.contractor.infrastructure.ContractState.PENDING
import com.orangefox.ripple.reservations.contractor.infrastructure.ContractorRepository
import com.orangefox.ripple.reservations.contractor.model.Contractor
import com.orangefox.ripple.reservations.contractor.model.Contractors

class ContractorRepository(val entityRepository: ContractorRepository): Contractors {
  override fun find(contractor: AgencyId): Contractor? {
    return entityRepository.findById(contractor.id).map { ContractorMapper().toDomain(it) }.orElse(null)
  }

  override fun save(contractor: Contractor) {
    val entity = ContractorMapper().toEntity(contractor)
    entityRepository.save(entity)
  }

  override fun findContractor(contract: ContractId): Contractor? {
    val contractor = entityRepository.findContractor(contract.id)
    return find(AgencyId(contractor))
  }

  override fun handle(event: ContractActivated) {
    val contractor = entityRepository.findById(event.contractorId).orElseThrow {
      IllegalStateException("Contractor with id ${event.contractorId} could not be found while handling ContractActivated event in reservation context")
    }

    val contractIndex = contractor.contracts.indexOfFirst { it.id == event.contractId }
    if(contractIndex >= 0) {
      val root = contractor.copy(contracts = contractor.contracts.toMutableList().apply {
        this[contractIndex] = this[contractIndex].copy(state = ContractState.ACTIVE)
      }.toSet())
      entityRepository.save(root)
    }
  }

  override fun handle(event: ContractSuspended) {
    val contractor = entityRepository.findById(event.contractorId).orElseThrow {
      IllegalStateException("Contractor could not be found while handling ContractActivated event")
    }

    val index = contractor.contracts.indexOfFirst { contractor.id == event.contractId }
    if(index >= 0) {
      contractor.contracts.toMutableList().apply {
        this[index] = this[index].copy(state = ContractState.SUSPENDED)
      }
    }
    entityRepository.save(contractor)

  }

  override fun handle(event: ContractCreated) {
    val contractor = entityRepository.findById(event.contractorId).orElseThrow {
      IllegalStateException("Contractor could not be found while handling ContractActivated event")
    }
    val updated = contractor.copy(contracts = contractor.contracts.toMutableSet().apply {
      this.add(
        Contract(
          id = event.contractId,
          consolidator = event.contract.consolidator.uuid,
          state = PENDING,
          accessPoints = event.contract.accessPoints.map { AccessPointRef(it.accessPoint) }.toSet(),
          policies = event.contract.policies.map { ContractPolicy(it.policy) }.toSet(),
          balance = event.contract.balance
        ).isNew(true)
      )
    }).isNew(false)
    entityRepository.save(updated)
  }

}
