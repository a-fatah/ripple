package com.orangefox.ripple.reservations.contractor.model

import com.orangefox.ripple.consolidation.agency.model.AgencyId
import com.orangefox.ripple.consolidation.agency.model.AgencyName
import com.orangefox.ripple.consolidation.contract.model.ContractId
import com.orangefox.ripple.consolidation.policy.model.PolicyId


class Contractor(
  val id: AgencyId,
  val name: AgencyName,
  val contracts: Set<Contract> = emptySet(),
  val policies: List<PolicyId> = emptyList()
) {
  val agencyId = id.id
}

open class Contract(val id: ContractId, val consolidator: AgencyId, val balance: Balance) {
  val contractId = id.id
}
