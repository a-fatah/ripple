package com.orangefox.ripple.consolidation.contract.model

import com.orangefox.ripple.consolidation.policy.model.Policy
import com.orangefox.ripple.reservations.farequote.model.Decimal
import io.vavr.control.Option
import java.math.BigDecimal
import java.time.LocalDate

class ContractCode(val code: String)
class StartDate(val date: LocalDate)
class EndDate(val date: LocalDate)
class ValueCap(val value: BigDecimal)
class NumberOfDays(val days: Long)

class ActiveContract(
  id: ContractId,
  contractor: Contractor,
  consolidator: Consolidator,
  code: ContractCode,
  val period: ContractPeriod = ContractPeriod(LocalDate.now(), NumberOfDays(30)),
  val valueCap: ValueCap,
  val balance: Balance = Balance(BigDecimal(0)),
  val meta: Set<ContractMeta> = emptySet(),
  val policies: List<Policy>
): Contract(id, contractor, consolidator, code) {

  val endDate: LocalDate = period.start.plusDays(period.lengthInDays)
  private val isRecurring: Boolean = meta.any { it is Recurring }
  private val recurringMeta = ((meta.find { it is Recurring } as? Recurring)?.strategy as? NumberOfCycles)
  private val canExtend = recurringMeta?.currentCycle != recurringMeta?.cycles
  val isChild = meta.any { it is Child }
  val parent = (meta.find { it is Child } as? Child)?.parent

  fun performTransaction(amount: Decimal): List<ContractEvent> {
    if (isChild) {
      if (parent is ActiveContract) {
        val parentEvents = parent.performTransaction(amount)
        if (parentEvents.any { it is TransactionFailed }) {
          return failTransaction(amount, "Failure at a top-level contract")
            .plus(parentEvents)
        }
        return listOf(
          TransactionPerformed(contractId, amount)
        ).plus(parentEvents)
      }
    } else {
      if (balance.amount >= amount) {
        return listOf(TransactionPerformed(contractId, amount))
      }
      return failTransaction(amount, "balance not sufficient")
    }
    return failTransaction(amount, "parent contract is not active")
  }

  private fun failTransaction(amount: Decimal, reason: String): List<TransactionFailed> {
    return listOf(TransactionFailed(contractId, amount, reason))
  }

  fun expire(currentDate: LocalDate): Option<ContractEvent> {
    if (currentDate.isAfter(endDate)) {
      return Option.of(ContractExpired(contractId))
    }
    return Option.none()
  }

  fun extend(currentDate: LocalDate): Option<ContractExtended> {
    if (currentDate.isAfter(endDate) && isRecurring && canExtend) {
      return Option.of(ContractExtended(contractId))
    }
    return Option.none()
  }

}
