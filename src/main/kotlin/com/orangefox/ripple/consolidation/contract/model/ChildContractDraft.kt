package com.orangefox.ripple.consolidation.contract.model

import java.time.LocalDate.now

class ChildContractDraft(
  val parent: Contract,
  id: ContractId,
  contractor: Contractor,
  consolidator: Consolidator,
  code: ContractCode,
  period: ContractPeriod = ContractPeriod(now(), NumberOfDays(30)),
  valueCap: ValueCap,
  haps: NumberOfHaps,
  meta: Set<ContractMeta>
): ContractDraft(id, contractor, consolidator, code, period, valueCap, meta, haps) {

  override fun validate(): ContractEvent {
    if (parent is BlockedContract) {
      return ContractBlocked(contractId, reason = "Contract blocked because of its parent being blocked")
    }
    if (parent is ActiveContract) {
      check(parent.meta.any { it is Descendible }) {
        return ContractInvalidated(contractId, reason = "Parent contract does not allow distribution")
      }
      check(parent.balance.amount >= valueCap.value) {
        return ContractInvalidated(contractId, reason = "Value cap should be less than or equal to parent contract's available limit")
      }
    }
    return super.validate()
  }

}
