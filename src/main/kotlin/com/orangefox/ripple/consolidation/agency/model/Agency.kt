package com.orangefox.ripple.consolidation.agency.model

import com.orangefox.ripple.commons.models.CityCode
import com.orangefox.ripple.commons.models.CountryCode
import com.orangefox.ripple.commons.models.Email
import com.orangefox.ripple.commons.models.PhoneNumber
import com.orangefox.ripple.consolidation.policy.model.Policy
import java.util.*

open class Agency(
  val id: AgencyId,
  val name: AgencyName,
  val contact: Contact,
  val accessPoints: List<AccessPoint>,
  val policies: List<Policy>)

class Contact(val email: Email, val phone: PhoneNumber, val address: Address)
class AgencyId(val id: UUID)
class AgencyName(val name: String)
class Address(val street: String, val city: CityCode, country: CountryCode)
