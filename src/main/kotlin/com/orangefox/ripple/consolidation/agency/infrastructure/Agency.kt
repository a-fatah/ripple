package com.orangefox.ripple.consolidation.agency.infrastructure

import com.orangefox.ripple.commons.entities.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

data class Agency(
  @Id var id: UUID? = null,
  var name: String,
  var email: String,
  var phone: String,
  var address: Address,
  var active: Boolean = true,
  var accessPoints: Set<AccessPoint> = emptySet(),
  var policies: Set<PolicyRef> = emptySet()
)

@Table("AGENCY_POLICY")
data class PolicyRef(
  val policy: UUID
) {
  @Id @Column("AGENCY") var id: UUID? = null
}

data class Address(
  @Id @Column("AGENCY") var id: UUID? = null,
  var street: String,
  var city: String,
  var country: String
): Entity<Address, UUID>(id)
