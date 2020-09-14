package com.orangefox.ripple.consolidation.contract.infrastructure

import com.orangefox.ripple.app.events.publisher.DomainEvents
import com.orangefox.ripple.consolidation.contract.model.ContractCreated
import org.springframework.data.rest.core.annotation.HandleAfterCreate
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component

@Component
@RepositoryEventHandler
class ContractRepositoryRestEventHandler(val domainEvents: DomainEvents) {

  @HandleAfterCreate
  fun handleContractCreate(contract: Contract) {
    domainEvents.publish(ContractCreated(contract))
  }

}
