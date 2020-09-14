package com.orangefox.ripple.consolidation.contract.model


class SuspendedContract(
  id: ContractId,
  contractor: Contractor,
  consolidator: Consolidator,
  code: ContractCode
): Contract(id, contractor, consolidator, code)
