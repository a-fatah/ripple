package com.orangefox.ripple.consolidation.agency.infrastructure

import com.orangefox.ripple.app.events.publisher.DomainEvents
import com.orangefox.ripple.reservations.contractor.model.AgencyCreated
import mu.KLogging
import org.springframework.data.rest.core.annotation.HandleAfterCreate
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component

@Component
@RepositoryEventHandler
class AgencyRestEventHandler(val domainEvents: DomainEvents) {

  @HandleAfterCreate
  fun handleContractCreate(agency: Agency) {
    domainEvents.publish(AgencyCreated(agency))
  }

  companion object: KLogging()

}
