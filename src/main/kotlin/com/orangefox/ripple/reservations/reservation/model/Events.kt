package com.orangefox.ripple.reservations.reservation.model

import com.orangefox.ripple.app.events.publisher.DomainEvent
import com.orangefox.ripple.reservations.reservation.infrastructure.UniversalRecord

class UniversalRecordCreated(val universalRecord: UniversalRecord): DomainEvent(universalRecord.id!!)
class UniversalRecordCancelled(val universalRecord: UniversalRecord): DomainEvent(universalRecord.id!!)
