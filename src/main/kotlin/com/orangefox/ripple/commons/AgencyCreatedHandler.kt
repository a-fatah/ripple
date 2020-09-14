package com.orangefox.ripple.commons

import com.orangefox.ripple.app.events.publisher.DomainEvents
import com.orangefox.ripple.commons.entities.User
import com.orangefox.ripple.commons.repositories.jdbc.UserRepository
import com.orangefox.ripple.reservations.contractor.model.AgencyCreated
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import java.util.UUID.randomUUID
import javax.annotation.PostConstruct

@Component
class AgencyCreatedHandler(
  private val users: UserRepository,
  private val context: ApplicationContext,
  private val domainEvents: DomainEvents
): ApplicationListener<AgencyCreated> {

  @PostConstruct
  fun register() {
    context.parent?.let {
      it as AbstractApplicationContext
      it.addApplicationListener(this)
    }

  }

  override fun onApplicationEvent(event: AgencyCreated) {
    val agency = event.agency
    val generatedPassword = "{noop}abc@1234" // TODO generate secure password
    val user = User(randomUUID(), agency.email, generatedPassword, agency.id!!, emptySet(), true)
    users.save(user)
    domainEvents.publish(UserCreated(user))
  }

}
