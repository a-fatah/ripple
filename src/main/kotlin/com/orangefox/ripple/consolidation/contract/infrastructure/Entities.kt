package com.orangefox.ripple.consolidation.contract.infrastructure

import com.orangefox.ripple.commons.entities.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class Contract(
  @Id var id: UUID? = null,
  var code: String,
  val parent: UUID? = null,
  val type: ContractType,
  val consolidator: ConsolidatorRef,
  val contractor: ContractorRef,
  val startDate: LocalDate,
  var periodLength: Long,
  var valueCap: BigDecimal,
  var locked: Boolean = false,
  var settlementPeriod: Int,
  var recurrent: Boolean,
  var numberOfCycles: Long,
  var currentCycle: Long,
  var allowDistribution: Boolean,
  var fareReview: Boolean = false,
  var allowVoid: Boolean = true,
  var allowRefund: Boolean = true,
  var allowExchange: Boolean = true,
  var refundReview: Boolean = false,
  var allowRevalidation: Boolean = true,
  var voidCharges: BigDecimal = BigDecimal(0.0),
  var refundCharges: BigDecimal = BigDecimal(0.0),
  var exchangeCharges: BigDecimal = BigDecimal(0.0),
  var allowTerminalAccess: Boolean = true,
  var state: ContractState,
  var policies: MutableSet<PolicyRef> = mutableSetOf(),
  var accessPoints: MutableSet<AccessPointRef> = mutableSetOf(),
  var transactions: MutableSet<Transaction> = mutableSetOf(),
  var payments: MutableSet<Payment> = mutableSetOf()
) {
  @Transient private val totalPayment: BigDecimal = payments.map { it.amount }.fold(ZERO) { a, b -> a + b }
  @Transient private val totalTransactions: BigDecimal = transactions.map { it.amount }.fold(ZERO) { a, b -> a + b }
  @Transient val balance: BigDecimal = totalPayment - totalTransactions
}

enum class ContractType { CREDIT, DEBIT }

@Table("CONTRACT_TRANSACTION")
data class Transaction(
  @Id @Column("CONTRACT") var id: UUID? = null,
  val timestamp: LocalDateTime = LocalDateTime.now(),
  val amount: BigDecimal
)

@Table("CONTRACT_ACCESS_POINT")
data class AccessPointRef(val accessPoint: UUID) {
  @Id @Column("CONTRACT") var id: UUID? = null
}

enum class ContractState {
  DRAFT, INVALID, ACTIVE, EXPIRED, SUSPENDED, BLOCKED, CLOSED
}
enum class CalculationStrategy {
  FLAT, PERCENTAGE_OF_BASE_FARE, PERCENTAGE_OF_TOTAL_AMOUNT, PERCENTAGE_OF_TOTAL_TAXES
}

@Table("CONTRACT_POLICY")
data class PolicyRef(
  val policy: UUID
) {
  @Id @Column("CONTRACT") var id: UUID? = null
}

@Table("CONTRACT_CONSOLIDATOR")
data class ConsolidatorRef(@Id @Column("CONTRACT") var id: UUID? = null, @Column("CONSOLIDATOR") val uuid: UUID)

@Table("CONTRACT_CONTRACTOR")
data class ContractorRef(@Id @Column("CONTRACT") var id: UUID? = null, @Column("CONTRACTOR") val uuid: UUID)

@Table("CONTRACT_PAYMENT")
data class Payment(@Id var id: UUID? = UUID.randomUUID(), val amount: BigDecimal, val date: LocalDate): Entity<Payment, UUID>(id)
