package com.orangefox.ripple.reservations.contractor.model

import com.orangefox.ripple.consolidation.agency.model.AgencyId
import com.orangefox.ripple.consolidation.contract.model.ContractActivated
import com.orangefox.ripple.consolidation.contract.model.ContractCreated
import com.orangefox.ripple.consolidation.contract.model.ContractId
import com.orangefox.ripple.consolidation.contract.model.ContractSuspended

interface Contractors {
  fun find(id: AgencyId): Contractor?
  fun findContractor(id: ContractId): Contractor?
  fun save(contractor: Contractor)
  fun handle(event: ContractActivated)
  fun handle(event: ContractSuspended)
  fun handle(event: ContractCreated)
}
