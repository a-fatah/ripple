package com.orangefox.ripple.consolidation.agency.model

import com.orangefox.ripple.commons.models.City
import com.orangefox.ripple.commons.models.CityCode

interface Cities {
  fun findCity(code: CityCode): City?
}
