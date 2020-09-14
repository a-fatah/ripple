package com.orangefox.ripple.app.events.publisher

import mu.KLogging
import org.springframework.context.ApplicationContext


class NativeApplicationEventPublisher(val applicationContext: ApplicationContext): DomainEvents {

  override fun publish(event: DomainEvent) {
    applicationContext.parent?.let { it.publishEvent(event) } ?: logger.warn {
        "No parent ApplicationContext registered! Modules won't coordinate with each other"
    }
  }

  companion object logging: KLogging()
}
