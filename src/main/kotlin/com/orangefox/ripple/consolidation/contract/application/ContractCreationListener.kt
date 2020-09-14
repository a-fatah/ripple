package com.orangefox.ripple.consolidation.contract.application

import com.orangefox.ripple.consolidation.contract.model.*
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class ContractCreationListener(private val context: ApplicationContext, private val contracts: Contracts):
  ApplicationListener<ContractCreated>, Ordered {

  @PostConstruct
  fun register() {
    context.parent?.let {
      it as AbstractApplicationContext
      it.addApplicationListener(this)
    }
  }

  override fun onApplicationEvent(event: ContractCreated) {
    val contract = contracts.findById(ContractId(event.contract.id!!))
    contract?.let {
      when(it) {
        is ContractDraft -> it.validate()
        is ChildContractDraft -> it.validate()
        else -> throw IllegalStateException("ContractCreated event was fired for a contract which is not a draft")
      }
    }.let {
      when(it) {
        is ContractInvalidated -> contracts.publish(it)
        is ContractSuspended -> contracts.publish(it)
        is ContractActivated -> contracts.publish(it)
      }
    }
  }

  override fun getOrder(): Int { // TODO use annotation
    return 1
  }
}
