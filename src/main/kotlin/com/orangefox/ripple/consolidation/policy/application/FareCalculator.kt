package com.orangefox.ripple.consolidation.policy.application

import com.orangefox.ripple.consolidation.agency.model.Agencies
import com.orangefox.ripple.consolidation.agency.model.AgencyId
import com.orangefox.ripple.consolidation.contract.model.ActiveContract
import com.orangefox.ripple.consolidation.contract.model.ContractId
import com.orangefox.ripple.consolidation.contract.model.Contracts
import com.orangefox.ripple.consolidation.policy.model.Commission
import com.orangefox.ripple.consolidation.policy.model.Discount
import com.orangefox.ripple.consolidation.policy.model.Policy
import com.orangefox.ripple.reservations.farequote.model.Fare
import java.time.LocalDate

class FareCalculator(val contracts: Contracts, val agencies: Agencies) {

  fun calculateFare(fare: Fare, contractId: ContractId): Fare {
    return adjustDiscounts(adjustCommissions(fare, contractId), contractId)
  }

  fun calculateFare(fare: Fare, agencyId: AgencyId): Fare {
    return adjustDiscounts(adjustCommissions(fare, agencyId), agencyId)
  }

  fun adjustCommissions(fare: Fare, agencyId: AgencyId): Fare {
    val agency = agencies.findAgency(agencyId)
      ?: throw IllegalStateException("Could not find Agency while adjusting fare")
    return doAdjustCommissions(doAdjustCommissions(fare, agency.policies), agency.policies)
  }

  fun adjustDiscounts(fare: Fare, agencyId: AgencyId): Fare {
    val agency = agencies.findAgency(agencyId)
      ?: throw IllegalStateException("Could not find Agency while adjusting fare")
    return doAdjustDiscounts(doAdjustCommissions(fare, agency.policies), agency.policies)
  }

  fun adjustCommissions(fare: Fare, contractId: ContractId): Fare {
    val contract = contracts.findById(contractId)
    contract as ActiveContract
    if (contract.isChild) { // add commissions from top-level contracts
      return doAdjustCommissions(adjustCommissions(fare, contract.parent!!.id), contract.policies)
    }
    return doAdjustCommissions(fare, contract.policies)
  }

  fun adjustDiscounts(fare: Fare, contractId: ContractId): Fare {
    val contract = contracts.findById(contractId)
    contract as ActiveContract
    return doAdjustDiscounts(fare, contract.policies)
  }

  private fun applicableToday(policy: Policy): Boolean {
    val today = LocalDate.now()
    return today.isAfter(policy.startDate) && today.isBefore(policy.endDate)
  }

  private fun doAdjustCommissions(fare: Fare, policies: List<Policy>): Fare {
    return policies.filter { applicableToday(it) }.fold(fare) { fare, policy ->
      policy.fareModifiers.filter { it is Commission }.fold(fare) { fare, commission ->
        commission.modifyFare(fare)
      }
    }
  }

  private fun doAdjustDiscounts(fare: Fare, policies: List<Policy>): Fare {
    return policies.filter { applicableToday(it) }.fold(fare) { fare, policy ->
      policy.fareModifiers.filter { it is Discount }.fold(fare) { fare, discount ->
        discount.modifyFare(fare)
      }
    }
  }

}
