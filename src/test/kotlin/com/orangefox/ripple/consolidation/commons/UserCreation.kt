package com.orangefox.ripple.consolidation.commons

import com.ninjasquad.springmockk.MockkBean
import com.orangefox.ripple.app.events.publisher.DomainEvent
import com.orangefox.ripple.app.events.publisher.DomainEvents
import com.orangefox.ripple.commons.CommonsConfiguration
import com.orangefox.ripple.commons.UserCreated
import com.orangefox.ripple.consolidation.agency.infrastructure.Address
import com.orangefox.ripple.consolidation.agency.infrastructure.Agency
import com.orangefox.ripple.reservations.contractor.model.AgencyCreated
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CommonsConfiguration::class])
internal class UserCreationTest {

  @Autowired
  lateinit var context: ApplicationContext

  @MockkBean
  lateinit var domainEvents: DomainEvents

  @Test
  fun `creates Admin user when Agency is created`() {
    val event = slot<DomainEvent>()

    every { domainEvents.publish(capture(event)) } just Runs

    val agency = Agency(
      UUID.randomUUID(),
      "ABC",
      "abc@example.com",
      "03102323429",
      Address(street = "Test", city = "KHI", country = "PK")
    )

    context.publishEvent(AgencyCreated(agency))

    assertThat(event.captured).isInstanceOf(UserCreated::class.java)
    val userCreated = event.captured as UserCreated
    assertThat(userCreated.user).hasFieldOrPropertyWithValue("userName", "abc@example.com")
  }

}
