package com.orangefox.ripple.reservations.contractor.model

import com.orangefox.ripple.consolidation.agency.model.AccessPointId
import com.orangefox.ripple.consolidation.agency.model.AgencyId
import com.orangefox.ripple.consolidation.contract.model.ContractId
import com.orangefox.ripple.consolidation.policy.model.PolicyId
import java.math.BigDecimal


class ActiveContract(
  id: ContractId,
  consolidator: AgencyId,
  balance: Balance,
  val accessPoints: List<AccessPointId>,
  val policies: List<PolicyId>
): Contract(id, consolidator, balance) {

}

class InactiveContract(id: ContractId, consolidator: AgencyId, balance: Balance): Contract(id, consolidator, balance)

class Balance(val amount: BigDecimal)
