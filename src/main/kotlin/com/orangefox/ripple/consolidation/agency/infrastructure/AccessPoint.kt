package com.orangefox.ripple.consolidation.agency.infrastructure

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.util.*

data class AccessPoint(
  val name: String,
  val credentials: Credentials,
  val provider: Provider
) {
  @Id
  var id: UUID? = UUID.randomUUID()
}

enum class Provider { GALILEO, WORLDSPAN, APPOLO, AXESS, ACH, RCS, SABRE, AMADEUS }

enum class CredentialType { HAP, USERNAME_PASSWORD }

data class Credentials(
  val type: CredentialType,
  val username: String,
  val password: String,
  val targetBranch: String
) {
  @Id
  @Column("ACCESS_POINT") var id: UUID? = null
}
