package com.orangefox.ripple.reservations.contractor.application

import com.orangefox.ripple.consolidation.contract.model.ContractActivated
import com.orangefox.ripple.reservations.contractor.model.Contractors
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class ContractActivatedHandler(
  private val contractors: Contractors,
  private val context: ApplicationContext
): ApplicationListener<ContractActivated> {

  @PostConstruct
  fun register() {
    context.parent?.let {
      it as AbstractApplicationContext
      it.addApplicationListener(this)
    }
  }

  override fun onApplicationEvent(event: ContractActivated) {
    contractors.handle(event)
  }

}
