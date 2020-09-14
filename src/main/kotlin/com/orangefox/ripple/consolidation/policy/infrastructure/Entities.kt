package com.orangefox.ripple.consolidation.policy.infrastructure

import com.orangefox.ripple.consolidation.contract.infrastructure.CalculationStrategy
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class Policy(
  val startDate: LocalDate,
  val endDate: LocalDate,
  var commissions: Set<Commission> = emptySet(),
  var discounts: Set<Discount> = emptySet(),
  var allowedAirlines: Set<AllowedAirline> = emptySet(),
  var blockedSectors: Set<BlockedSector> = emptySet(),
  var blockedAirlines: Set<BlockedAirline> = emptySet(),
  var blockedSectorsOnAirlines: Set<BlockedSectorOnAirline> = emptySet()
) {
  @Id
  var id: UUID? = null
}

@Table("POLICY_ALLOWED_AIRLINE")
data class AllowedAirline(
  val airline: String
) {
  @Id @Column("POLICY") var id: UUID? = null
}

@Table("POLICY_BLOCKED_SECTOR")
data class BlockedSector(
  var name: String,
  val origin: String,
  val dest: String
) {
  @Id
  @Column("POLICY") var id: UUID? = null
}

@Table("POLICY_BLOCKED_AIRLINE")
data class BlockedAirline(
  var name: String,
  val airline: String
) {
  @Id
  @Column("POLICY") var id: UUID? = null
}

@Table("POLICY_BLOCKED_SECTOR_ON_AIRLINE")
data class BlockedSectorOnAirline(
  var name: String,
  val origin: String,
  val dest: String,
  val airline: String
) {
  @Id
  @Column("POLICY") var id: UUID? = null
}

@Table("POLICY_DISCOUNT")
data class Discount(
  @Id @Column("POLICY") var id: UUID? = null,
  val name: String,
  val bookingClass: String?,
  val strategy: CalculationStrategy,
  val value: BigDecimal
)

@Table("POLICY_COMMISSION")
data class Commission(
  @Id @Column("POLICY") var id: UUID? = null,
  val name: String,
  val bookingClass: String?,
  val strategy: CalculationStrategy,
  val value: BigDecimal
)
