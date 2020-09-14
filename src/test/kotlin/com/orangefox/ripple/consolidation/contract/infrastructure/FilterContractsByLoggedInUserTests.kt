package com.orangefox.ripple.consolidation.contract.infrastructure

import com.orangefox.ripple.commons.CommonsConfiguration
import com.orangefox.ripple.commons.RippleUserDetailsService
import com.orangefox.ripple.commons.entities.User
import com.orangefox.ripple.consolidation.ConsolidationConfiguration
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.ContextHierarchy
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID.randomUUID

val contractorId = randomUUID()
val consolidatorId = randomUUID()

@ExtendWith(SpringExtension::class)
@ContextHierarchy(
  ContextConfiguration(classes = [CommonsConfiguration::class]),
  ContextConfiguration(classes = [ConsolidationConfiguration::class])
)
internal open class ContractFilteringTests @Autowired constructor(
  val contractsRepository: ContractsRepository
) {

  @Test
  @WithUserDetails("contractor", userDetailsServiceBeanName = "testUserDetailsService")
  fun `loads currently logged in contractor's contracts`() {
    // arrange
    var contract = Contract(
      type = ContractType.DEBIT,
      code = "TEST0001",
      consolidator = ConsolidatorRef(uuid = randomUUID()),
      contractor = ContractorRef(uuid = contractorId),
      startDate = LocalDate.now(),
      periodLength = 30,
      recurrent = true,
      numberOfCycles = 2,
      currentCycle = 1,
      settlementPeriod = 30,
      valueCap = BigDecimal.valueOf(100.0),
      allowDistribution = false,
      state = ContractState.ACTIVE,
      accessPoints = mutableSetOf(AccessPointRef(randomUUID())),
      transactions = mutableSetOf(
        Transaction(amount = BigDecimal.valueOf(100.0)),
        Transaction(amount = BigDecimal.valueOf(200.0))
      )
    )

    var otherContracts = (1..10).map { contract.copy(contractor = ContractorRef(uuid = randomUUID())) }
    contract = contractsRepository.save(contract)
    otherContracts = contractsRepository.saveAll(otherContracts).toList() // other contracts not related to contractor currently logged in

    // act
    val contracts = contractsRepository.findContractsForContractor()

    // assert
    assertThat(contracts).containsExactly(ContractorRef(contract.id!!, contractorId))
  }

  @Test
  @WithUserDetails("consolidator", userDetailsServiceBeanName = "testUserDetailsService")
  fun `loads currently logged in consolidator's contracts`() {
    // arrange
    var contract = Contract(
      type = ContractType.DEBIT,
      code = "TEST0001",
      consolidator = ConsolidatorRef(uuid = consolidatorId),
      contractor = ContractorRef(uuid = randomUUID()),
      startDate = LocalDate.now(),
      periodLength = 30,
      recurrent = true,
      numberOfCycles = 2,
      currentCycle = 1,
      settlementPeriod = 30,
      valueCap = BigDecimal.valueOf(100.0),
      allowDistribution = false,
      state = ContractState.ACTIVE,
      accessPoints = mutableSetOf(AccessPointRef(randomUUID())),
      transactions = mutableSetOf(
        Transaction(amount = BigDecimal.valueOf(100.0)),
        Transaction(amount = BigDecimal.valueOf(200.0))
      )
    )

    var otherContracts = (1..10).map { contract.copy(consolidator = ConsolidatorRef(uuid = randomUUID())) }
    contract = contractsRepository.save(contract)
    otherContracts = contractsRepository.saveAll(otherContracts).toList() // other contracts not related to contractor currently logged in

    // act
    val contracts = contractsRepository.findContractsForConsolidator()

    // assert
    assertThat(contracts).containsExactly(ConsolidatorRef(contract.id!!, consolidatorId))
  }

  @Configuration
  open class MockSecurityConfiguration {

    @Bean
    open fun testUserDetailsService(): UserDetailsService {
      val mockService = mockk<RippleUserDetailsService>()
      every { mockService.loadUserByUsername("consolidator") } returns User(
        userName = "consolidator",
        pass = "[PROTECTED]",
        agency = consolidatorId,
        enabled = true
      )
      every { mockService.loadUserByUsername("contractor") } returns User(
        userName = "contractor",
        pass = "[PROTECTED]",
        agency = contractorId,
        enabled = true
      )
      return mockService
    }
  }

}
