package com.orangefox.ripple.consolidation.contract.application

import com.orangefox.ripple.app.events.publisher.DomainEvents
import com.orangefox.ripple.consolidation.contract.infrastructure.ContractState
import com.orangefox.ripple.consolidation.contract.infrastructure.ContractsRepository
import com.orangefox.ripple.consolidation.contract.infrastructure.DomainMapper
import com.orangefox.ripple.consolidation.contract.model.*
import org.springframework.stereotype.Component
import com.orangefox.ripple.consolidation.contract.infrastructure.Contract as ContractEntity

@Component
class ContractDatabaseRepository(
  private val contractRepository: ContractsRepository,
  private val domainEvents: DomainEvents,
  private val domainMapper: DomainMapper): Contracts {

  override fun findAll(): List<Contract> {
    return contractRepository.findAll().map { toDomain(it) }
  }

  override fun findById(contract: ContractId): Contract? {
    return contractRepository.findById(contract.id).map { toDomain(it) }.orElse(null)
  }

  private fun toDomain(contract: ContractEntity): Contract {
    return domainMapper.mapToDomain(contract)
  }

  override fun publish(event: ContractActivated) {

    val entity = contractRepository.findById(event.aggregateId)
    entity.map { it.copy(state = ContractState.ACTIVE) }
      .map { contractRepository.save(it) }
      .orElseThrow { IllegalStateException("Could not find contract while reacting to ContractActivated event") }

    domainEvents.publish(event)

  }

  override fun publish(event: ContractSuspended) {
    val entity = contractRepository.findById(event.aggregateId)
     entity.map { it.copy(state = ContractState.SUSPENDED) }
      .map { contractRepository.save(it) }
      .orElseThrow { IllegalStateException("Could not find contract while reacting to ContractSuspended event") }

    domainEvents.publish(event)
  }

  override fun publish(event: ContractInvalidated) {
    val entity = contractRepository.findById(event.aggregateId)
    entity.map { it.copy(state = ContractState.INVALID) }
      .map { contractRepository.save(it) }
      .orElseThrow { IllegalStateException("Could not find contract while reacting to ContractInvalidated event") }

    domainEvents.publish(event)
  }

  override fun publish(event: ContractExpired) {
    val contract = findById(event.contractId())
    contract?.let {
      val entity = contractRepository.findById(event.aggregateId)
      val updated = entity.map { it.copy(state = ContractState.EXPIRED) }.get()
      contractRepository.save(updated)
      domainEvents.publish(event)
    } ?: throw IllegalStateException("Could not find contract while reacting to ContractExpired event")
  }

  override fun publish(event: ContractExtended) {
    val contract = contractRepository.findById(event.aggregateId)
    contract.ifPresent {
      val nextCycle = it.currentCycle + 1
      val newEndDate = it.startDate.plusDays(nextCycle * it.periodLength)
      val newContract = it.copy(
        currentCycle = nextCycle,
        state = ContractState.ACTIVE
      )
      contractRepository.save(newContract)
      domainEvents.publish(event)
    }
  }

}
