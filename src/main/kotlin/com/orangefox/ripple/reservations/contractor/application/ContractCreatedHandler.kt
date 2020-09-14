package com.orangefox.ripple.reservations.contractor.application

import com.orangefox.ripple.consolidation.contract.model.ContractCreated
import com.orangefox.ripple.reservations.contractor.model.Contractors
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class ContractCreatedHandler(
  private val contractors: Contractors,
  private val context: ApplicationContext
): ApplicationListener<ContractCreated>, Ordered {

  @PostConstruct
  fun register() {
    context.parent?.let {
      it as AbstractApplicationContext
      it.addApplicationListener(this)
    }
  }

  override fun onApplicationEvent(event: ContractCreated) {
    contractors.handle(event)
  }

  override fun getOrder(): Int {
    return 0
  }

}
