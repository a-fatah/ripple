package com.orangefox.ripple.consolidation.contract.application

import com.orangefox.ripple.consolidation.contract.model.*

class ContractsManager(private val contracts: Contracts, private val dateProvider: DateProvider) {

  fun expireContracts(): List<ContractEvent> {
    return contracts.findAll()
      .filterIsInstance<ActiveContract>()
      .map { it.expire(dateProvider.getCurrentDate()) }
      .filter { it.isDefined }
      .map { it.get() as ContractExpired }
  }

  fun extendContracts(): List<ContractEvent> {
    return contracts.findAll()
      .filterIsInstance<ActiveContract>()
      .map { it.extend(dateProvider.getCurrentDate()) }
      .filter { it.isDefined }
      .map { it.get() as ContractExtended }
  }

}
