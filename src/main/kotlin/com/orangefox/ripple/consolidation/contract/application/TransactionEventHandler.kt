package com.orangefox.ripple.consolidation.contract.application

import com.orangefox.ripple.consolidation.contract.infrastructure.ContractsRepository
import com.orangefox.ripple.consolidation.contract.infrastructure.Transaction
import com.orangefox.ripple.consolidation.contract.model.TransactionPerformed
import com.orangefox.ripple.consolidation.policy.application.FindPolicyById
import com.orangefox.ripple.consolidation.policy.model.PolicyId

class TransactionEventHandler(private val contracts: ContractsRepository, private val policies: FindPolicyById) {

  fun handleTransactionPerformed(event: TransactionPerformed) {
    val contract = contracts.findById(event.contractId).orElseThrow {
      IllegalStateException("Contract not found while handling TransactionPerformed")
    }

    contract.transactions.add(Transaction(amount = event.amount))
    contracts.save(contract)

    contract.policies.map { ref ->
      val policy = policies.find(PolicyId(ref.policy))
      policy?.let {
        it.fareModifiers.map {

        }
      }
    }

    // consolidator balance will be reduced by amount
    // contractor balance will also be reduced by amount
    // discount payable
    // discount receivable
    // commission payable
    // commission receivable
    // other charges
    // total payable
    // total receivable
  }

}
