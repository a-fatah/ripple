package com.orangefox.ripple.consolidation.contract.model

interface Contracts {
  fun findAll(): List<Contract>
  fun findById(id: ContractId): Contract?
  fun publish(event: ContractActivated)
  fun publish(event: ContractSuspended)
  fun publish(event: ContractInvalidated)
  fun publish(event: ContractExpired)
  fun publish(event: ContractExtended)
}
