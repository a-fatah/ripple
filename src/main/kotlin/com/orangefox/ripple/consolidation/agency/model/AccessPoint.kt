package com.orangefox.ripple.consolidation.agency.model

import java.util.*


open class AccessPoint(val id: AccessPointId, val name: AccessPointName, open val credentials: Credentials) {
  override fun equals(other: Any?): Boolean =
    if (other is AccessPoint) {
      id == other.id &&
        name == other.name &&
        credentials == other.credentials
    } else {
      false
    }

  val accessPointId = id.id

}

class TravelportUAPI(
  id: AccessPointId,
  name: AccessPointName,
  override val credentials: HostAccessProfile
): AccessPoint(id, name, credentials)

data class AccessPointId(val id: UUID)
data class AccessPointName(val name: String)
abstract class Credentials
data class HostAccessProfile(val username: String, val password: String, val branch: String): Credentials()
data class UsernamePassword(val username: String, val password: String): Credentials()
