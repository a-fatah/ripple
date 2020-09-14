package com.orangefox.ripple.consolidation.contract.application

import java.time.LocalDate

interface DateProvider {
  fun getCurrentDate(): LocalDate
}
