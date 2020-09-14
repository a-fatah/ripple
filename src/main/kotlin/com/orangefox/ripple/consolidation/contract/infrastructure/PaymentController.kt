package com.orangefox.ripple.consolidation.contract.infrastructure

import com.orangefox.ripple.app.events.publisher.DomainEvents
import com.orangefox.ripple.consolidation.contract.model.PaymentReceived
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class PaymentController(val contracts: ContractsRepository, val domainEvents: DomainEvents) {

  @PostMapping("/contracts/{id}/payment")
  fun createPayment(@PathVariable("id") id: UUID, @RequestBody payment: Payment): Contract {
    val contract = contracts.findById(id).orElseThrow {
      IllegalStateException("Contract with id ${id} not found")
    }
    val root = contract.copy(payments = contract.payments.toMutableSet().apply {
      this.add(payment)
    })
    val updated = contracts.save(root)
    domainEvents.publish(PaymentReceived(updated.id!!, updated.contractor.uuid, payment.amount))
    return updated
  }
}
