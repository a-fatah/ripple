package com.orangefox.ripple.consolidation.agency.model

import com.orangefox.ripple.consolidation.contract.model.Consolidator
import com.orangefox.ripple.consolidation.contract.model.Contractor

interface Agencies {
  fun findAgency(agency: AgencyId): Agency?
  fun findConsolidator(id: AgencyId): Consolidator?
  fun findContractor(id: AgencyId): Contractor?
  fun findAccessPoint(id: AccessPointId): AccessPoint?
}
