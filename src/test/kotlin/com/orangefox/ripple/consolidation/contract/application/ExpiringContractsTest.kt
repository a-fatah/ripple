package com.orangefox.ripple.consolidation.contract.application

import com.orangefox.ripple.app.events.publisher.DomainEvents
import com.orangefox.ripple.consolidation.agency.model.AgencyId
import com.orangefox.ripple.consolidation.contract.application.ContractEventsAssert.Companion.assertThat
import com.orangefox.ripple.consolidation.contract.model.*
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID.randomUUID

@ExtendWith(MockKExtension::class)
internal class ExpiringContractsTest {

  val contracts = mockk<Contracts>(relaxUnitFun = true)
  val dateProvider = mockk<DateProvider>()
  val domainEvents = mockk<DomainEvents>(relaxUnitFun = true)

  @Test
  fun `given current date is past contract's end date when scheduled job runs then it should expire contract`() {
    // arrange
    val expiredContractId = randomUUID()
    val contract = ActiveContract(
      id = ContractId(expiredContractId),
      contractor = Contractor(AgencyId(randomUUID())),
      consolidator = Consolidator(AgencyId(randomUUID()), NumberOfHaps(1)),
      code = ContractCode("TEST"),
      valueCap = ValueCap(BigDecimal(1000.0)),
      balance = Balance(BigDecimal(100.0)),
      period = ContractPeriod(LocalDate.now(), NumberOfDays(30)),
      policies = emptyList()
    )
    every { contracts.findAll() } returns listOf(contract)
    every { dateProvider.getCurrentDate() } returns contract.endDate.plusDays(1)

    // act
    val events = ContractsManager(contracts, dateProvider).expireContracts()

    // assert
    assertThat(events).containsContractExpiredEvent(expiredContractId)
  }

  @Test
  fun `given current date is before contract's end date when attempt to expire then it should not expire`() {
    // arrange
    val contract = ActiveContract(
      id = ContractId(randomUUID()),
      contractor = Contractor(AgencyId(randomUUID())),
      consolidator = Consolidator(AgencyId(randomUUID()), NumberOfHaps(1)),
      code = ContractCode("TEST"),
      valueCap = ValueCap(BigDecimal(1000.0)),
      balance = Balance(BigDecimal(100.0)),
      period = ContractPeriod(LocalDate.now(), NumberOfDays(30)),
      policies = emptyList()
    )
    every { contracts.findAll() } returns listOf(contract)
    every { dateProvider.getCurrentDate() } returns contract.endDate.minusDays(1)

    // act
    val events = ContractsManager(contracts, dateProvider).expireContracts()

    // assert
    Assertions.assertThat(events).isEmpty()
  }

}
