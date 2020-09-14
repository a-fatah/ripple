package com.orangefox.ripple.reservations.contractor.application

import com.orangefox.ripple.consolidation.agency.model.AgencyId
import com.orangefox.ripple.consolidation.agency.model.AgencyName
import com.orangefox.ripple.reservations.contractor.model.AgencyCreated
import com.orangefox.ripple.reservations.contractor.model.Contractor
import com.orangefox.ripple.reservations.contractor.model.Contractors
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class ContractorCreatedHandler(
  private val contractors: Contractors,
  private val context: ApplicationContext
): ApplicationListener<AgencyCreated> {

  @PostConstruct
  fun register() {
    context.parent?.let {
      it as AbstractApplicationContext
      it.addApplicationListener(this)
    }
  }

  override fun onApplicationEvent(event: AgencyCreated) {
    val agency = event.agency
    val name = AgencyName(agency.name)
    val id = AgencyId(agency.id!!)
    val contractor = Contractor(id, name)
    contractors.save(contractor)
  }

}
