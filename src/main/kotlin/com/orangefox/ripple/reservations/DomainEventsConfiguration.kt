package com.orangefox.ripple.reservations

import com.orangefox.ripple.app.events.publisher.DomainEvent
import com.orangefox.ripple.app.events.publisher.DomainEvents
import mu.KLogging
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class DomainEventsConfiguration(val context: ApplicationContext) {

  @Bean
  open fun domainEvents(): DomainEvents {
    return object: DomainEvents {
      override fun publish(event: DomainEvent) {
        context.parent?.let {
          it.publishEvent(event)
        } ?: logger.warn {
          "No parent ApplicationContext registered! Modules won't coordinate with each other"
        }
      }

    }
  }

  companion object: KLogging()
}
