package com.orangefox.ripple.app.events.publisher

import org.springframework.context.ApplicationEvent
import java.time.Instant
import java.time.Instant.now
import java.util.*

interface DomainEvents {
  fun publish(event: DomainEvent)
  fun publish(events: List<DomainEvent>) = events.forEach{ publish(it) }
}

abstract class DomainEvent(
  val aggregateId: UUID,
  val eventId: UUID = UUID.randomUUID(),
  val timestamp: Instant = now()
): ApplicationEvent(aggregateId) {

  fun normalize(): List<DomainEvent> {
    return listOf(this)
  }
}
