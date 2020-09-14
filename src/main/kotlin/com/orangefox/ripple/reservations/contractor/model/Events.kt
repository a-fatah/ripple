package com.orangefox.ripple.reservations.contractor.model

import com.orangefox.ripple.app.events.publisher.DomainEvent
import com.orangefox.ripple.consolidation.agency.infrastructure.Agency

class AgencyCreated(val agency: Agency) : DomainEvent(agency.id!!)
