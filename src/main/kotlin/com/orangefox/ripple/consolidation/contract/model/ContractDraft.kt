package com.orangefox.ripple.consolidation.contract.model

import java.time.LocalDate

open class ContractDraft(
  id: ContractId,
  contractor: Contractor,
  consolidator: Consolidator,
  code: ContractCode,
  val period: ContractPeriod = ContractPeriod(LocalDate.now(), NumberOfDays(30)),
  val valueCap: ValueCap,
  val meta: Set<ContractMeta> = emptySet(),
  val haps: NumberOfHaps
): Contract(id, contractor, consolidator, code) {

  private val contractorId = contractor.agency.id
  private val endDate: LocalDate = period.start.plusDays(period.lengthInDays)
  private val isDebitContract: Boolean = meta.any { it is DebitContract }

  open fun validate(): ContractEvent {
    check(consolidator.haps.count > 0) {
      return ContractInvalidated(contractId, reason = "Consolidator does not have access points")
    }
    check(haps.count > 0) {
      return ContractInvalidated(contractId, reason = "Contract is not assigned access points")
    }
    if (this.isDebitContract) {
      return ContractSuspended(contractId, contractorId, reason = "Contract suspended due to missing payment")
    }
    return ContractActivated(contractId, contractorId)
  }

}
