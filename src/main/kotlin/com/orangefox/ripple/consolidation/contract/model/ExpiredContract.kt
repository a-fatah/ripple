package com.orangefox.ripple.consolidation.contract.model

class ExpiredContract(
  id: ContractId,
  contractor: Contractor,
  consolidator: Consolidator,
  code: ContractCode
): Contract(id, contractor, consolidator, code) {

//  val isRecurring = meta.any { it is Recurring }
//  val recurringMeta: Recurring? = (meta.first { it is Recurring } as? Recurring)
//  val currentCycle: Long? = (recurringMeta?.strategy as? NumberOfCycles)?.currentCycle
//  val isNotLastCycle: Boolean = currentCycle !== (recurringMeta?.strategy as? NumberOfCycles)?.cycles
//
//  fun extendContract(): Option<ContractExtended> {
//
//    if (isRecurring && isNotLastCycle) {
//      return Option.of(ContractExtended(contractId))
//    }
//    return Option.none()
//  }

}
