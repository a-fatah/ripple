package com.orangefox.ripple.consolidation.policy.infrastructure.repository

import com.orangefox.ripple.commons.CommonsConfiguration
import com.orangefox.ripple.consolidation.ConsolidationConfiguration
import com.orangefox.ripple.consolidation.contract.infrastructure.CalculationStrategy
import com.orangefox.ripple.consolidation.policy.infrastructure.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.ContextHierarchy
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.time.LocalDate


@ExtendWith(SpringExtension::class)
@ContextHierarchy(
  ContextConfiguration(classes = [CommonsConfiguration::class]),
  ContextConfiguration(classes = [ConsolidationConfiguration::class])
)
internal open class AgencyJdbcRepositoryTest @Autowired constructor(
  val policyRepository: PolicyRepository
) {

  @Test
  fun `saves policy in the database`() {
    // arrange
    var policy = Policy(
      startDate = LocalDate.now(),
      endDate = LocalDate.now().plusMonths(1),
      commissions = setOf(
        Commission(name = "Flat 5% Commission", strategy = CalculationStrategy.FLAT, value = BigDecimal(5.0), bookingClass = "Y")
      ),
      discounts = setOf(
        Discount(name = "Flat 5% Discount", strategy = CalculationStrategy.FLAT, value = BigDecimal(5.0), bookingClass = "Y")
      ),
      allowedAirlines = setOf(
        AllowedAirline("PK")
      ),
      blockedSectors = setOf(
        BlockedSector("Block KHI-DXB", "KHI", "DXB")
      ),
      blockedSectorsOnAirlines = setOf(
        BlockedSectorOnAirline("Block KHI-DXB on PK", "KHI", "DXB", "PK")
      ),
      blockedAirlines = setOf(
        BlockedAirline("Block PK", "PK")
      )
    )

    // act
    policy = policyRepository.save(policy)

    // assert
    assertThat(policy.id).isNotNull()
  }

}
