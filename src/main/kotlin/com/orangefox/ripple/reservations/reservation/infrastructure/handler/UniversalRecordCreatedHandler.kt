package com.orangefox.ripple.reservations.reservation.infrastructure.handler

import com.orangefox.ripple.reservations.reservation.model.UniversalRecordCreated
import mu.KLogging
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


@Component
class UniversalRecordCreatedHandler(
  private val context: ApplicationContext): ApplicationListener<UniversalRecordCreated> {

  @PostConstruct
  fun register() {
    context.parent?.let {
      it as AbstractApplicationContext
      it.addApplicationListener(this)
    }
  }

  override fun onApplicationEvent(event: UniversalRecordCreated) {
    logger.info { "Universal Record created: ${event.universalRecord.locatorCode}" }
  }

  companion object logging: KLogging()
}
