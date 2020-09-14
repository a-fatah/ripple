package com.orangefox.ripple.consolidation.contract.application

import com.orangefox.ripple.consolidation.contract.model.ContractEvent
import com.orangefox.ripple.consolidation.contract.model.ContractExpired
import com.orangefox.ripple.consolidation.contract.model.ContractExtended
import org.assertj.core.api.AbstractAssert
import org.junit.jupiter.api.fail
import java.util.*

class ContractEventsAssert(events: List<ContractEvent>):
  AbstractAssert<ContractEventsAssert, List<ContractEvent>>(
    events, ContractEventsAssert::class.java) {

  fun containsContractExpiredEvent(expiredContractId: UUID): ContractEventsAssert {
    actual.firstOrNull { it is ContractExpired
      && it.aggregateId == expiredContractId }
      ?: failWithMessage("Actual list of event does not contain any instance of ContractExpired event with contract Id ${expiredContractId}")
    return this
  }

  fun containsContractExtendedEvent(extendedContractId: UUID): ContractEventsAssert {
    actual.firstOrNull { it is ContractExtended
      && it.aggregateId == extendedContractId }
      ?: failWithMessage("Actual list of event does not contain any instance of ContractExtended event with contract Id ${extendedContractId}")

    return this
  }

  fun doesNotContainRenewedEvent(renewedContract: UUID): ContractEventsAssert {
    return if (actual.filter {
      it is ContractExtended && it.aggregateId == renewedContract
    }.isEmpty())
      this
    else {
        fail {
            "Actual list of event contains instance(s) of ContractRenewed event with contract Id ${renewedContract}"
        }
    }
  }

  companion object {
    fun assertThat(actual: List<ContractEvent>): ContractEventsAssert {
      return ContractEventsAssert(actual)
    }
  }
}
