package com.orangefox.ripple.consolidation.agency.infrastructure.repository

import com.orangefox.ripple.commons.CommonsConfiguration
import com.orangefox.ripple.consolidation.ConsolidationConfiguration
import com.orangefox.ripple.consolidation.agency.infrastructure.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.ContextHierarchy
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.UUID.randomUUID

@ExtendWith(SpringExtension::class)
@ContextHierarchy(
  ContextConfiguration(classes = [CommonsConfiguration::class]),
  ContextConfiguration(classes = [ConsolidationConfiguration::class])
)
internal open class AgencyJdbcRepositoryTest @Autowired constructor(
  val agencyRepository: AgencyRepository
) {

  @Test
  fun `can save agency to database`() {
    // arrange
    var agency = Agency(
      name = "ABC Travels",
      email = "abc@travel.com",
      phone = "03133491912",
      address = Address(
        street = "DHA Phase 2",
        city = "KHI",
        country = "PK"
      ),
      accessPoints = setOf(
        AccessPoint(
          name = "Galileo Test",
          provider = Provider.GALILEO,
          credentials = Credentials(
            type = CredentialType.HAP,
            username = "***",
            password = "***",
            targetBranch = "***"
          )
        )
      ),
      policies = setOf(PolicyRef(randomUUID()))
    )

    // act
    agency = agencyRepository.save(agency)

    // assert
    assertThat(agency.id).isNotNull()
  }

  @Test
  fun `can update agency in database`() {
    // arrange
    var agency = Agency(
      name = "ABC Travels",
      email = "abc@travel.com",
      phone = "03133491912",
      address = Address(
        street = "DHA Phase 2",
        city = "KHI",
        country = "PK"
      ),
      accessPoints = setOf(
        AccessPoint(
          name = "Galileo Test",
          provider = Provider.GALILEO,
          credentials = Credentials(
            type = CredentialType.HAP,
            username = "***",
            password = "***",
            targetBranch = "***"
          )
        )
      )
    )

    // act
    agency = agencyRepository.save(agency)

    // assert
    assertThat(agency.id).isNotNull()
  }

}
