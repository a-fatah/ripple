package com.orangefox.ripple.reservations.contractor.infrastructure

import com.orangefox.ripple.commons.entities.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.util.*

data class Contractor(
  @Id var id: UUID? = null,
  val name: String,
  val contracts: Set<Contract> = emptySet(),
  val policies: Set<ContractorPolicy> = emptySet()
): Entity<Contractor, UUID>(id)

data class Contract(
  @Id var id: UUID? = null,
  val consolidator: UUID,
  val state: ContractState,
  val accessPoints: Set<AccessPointRef> = emptySet(),
  val policies: Set<ContractPolicy> = emptySet(),
  val balance: BigDecimal
): Entity<Contract, UUID>(id)

enum class ContractState {
  PENDING, INVALID, ACTIVE, EXPIRED, SUSPENDED, BLOCKED, CLOSED, INACTIVE //TODO remove unused states
}

@Table("ACCESS_POINT")
data class AccessPointRef(val ref: UUID) {
  @Id @Column("CONTRACT") var id: UUID? = null
}

data class ContractPolicy(val ref: UUID) {
  @Id @Column("CONTRACT") var id: UUID? = null
}


data class ContractorPolicy(val ref: UUID) {
  @Id @Column("CONTRACTOR") var id: UUID? = null
}
