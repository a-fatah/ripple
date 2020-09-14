package com.orangefox.ripple.reservations.reservation.infrastructure.gateway

import com.orangefox.ripple.reservations.reservation.infrastructure.Traveler
import com.orangefox.ripple.reservations.reservation.infrastructure.UniversalRecord
import java.util.*

interface CreateUniversalRecord {
  fun createUniversalRecord(fareQuote: UUID, travelers: Set<Traveler>): UniversalRecord
}

interface CancelUniversalRecord {
  fun cancelUniversalRecord(universalRecord: UUID): UniversalRecord
}
