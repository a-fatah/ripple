package com.orangefox.ripple.consolidation.contract.model

import com.orangefox.ripple.consolidation.agency.model.AgencyId
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

abstract class Contract(
  val id: ContractId,
  val contractor: Contractor,
  val consolidator: Consolidator,
  val code: ContractCode
) {
  val contractId = id.id
}

data class ContractId(val id: UUID)
data class Consolidator(val agency: AgencyId, val haps: NumberOfHaps = NumberOfHaps(1))
data class Contractor(val agency: AgencyId)
data class Guarantee(val amount: BigDecimal)

class ContractPeriod(val start: LocalDate, val periodLength: NumberOfDays) {
  val lengthInDays = periodLength.days
}

class NumberOfHaps(val count: Int)
class Balance(val amount: BigDecimal)

abstract class RecurringStrategy
class NumberOfCycles(val cycles: Long, val currentCycle: Long): RecurringStrategy()
