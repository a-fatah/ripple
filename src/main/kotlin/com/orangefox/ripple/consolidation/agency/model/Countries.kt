package com.orangefox.ripple.consolidation.agency.model

import com.orangefox.ripple.commons.models.Country
import com.orangefox.ripple.commons.models.CountryCode

interface Countries {
  fun findAll(): List<Country>
  fun findCountry(code: CountryCode): Country?
}
