package com.orangefox.ripple.consolidation.contract.application

import com.orangefox.ripple.app.events.publisher.DomainEvents
import com.orangefox.ripple.consolidation.contract.infrastructure.ContractsRepository
import com.orangefox.ripple.consolidation.contract.model.ContractActivated
import com.orangefox.ripple.consolidation.contract.model.PaymentReceived
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


@Component
class PaymentReceivedHandler(
        private val context: ApplicationContext,
        private val contracts: ContractsRepository,
        private val domainEvents: DomainEvents
): ApplicationListener<PaymentReceived> {

  @PostConstruct
  fun register() {
    context.parent?.let {
      it as AbstractApplicationContext
      it.addApplicationListener(this)
    }
  }

  override fun onApplicationEvent(event: PaymentReceived) {
    val contract = contracts.findById(event.contract).orElseThrow {
      IllegalStateException("Could not find Contract")
    }
    if (contract.balance >= contract.valueCap) {
      // TODO approve when payment is received by consolidator
      domainEvents.publish(ContractActivated(contract.id!!, contract.contractor.uuid))
    }
  }
}
