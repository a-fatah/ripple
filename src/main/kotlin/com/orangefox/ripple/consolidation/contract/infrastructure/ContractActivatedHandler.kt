package com.orangefox.ripple.consolidation.contract.infrastructure

import com.orangefox.ripple.consolidation.contract.model.ContractActivated
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class ContractActivatedHandler(
        private val contracts: ContractsRepository,
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
    val contract = contracts.findById(event.contractId).orElseThrow {
      IllegalStateException("Could not find Contract while trying to activate it")
    }
    contract.state = ContractState.ACTIVE
    contracts.save(contract)
  }

}
