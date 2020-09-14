package com.orangefox.ripple.consolidation.contract.application

import com.orangefox.ripple.app.events.publisher.DomainEvents
import com.orangefox.ripple.consolidation.agency.model.AgencyId
import com.orangefox.ripple.consolidation.contract.application.ContractEventsAssert.Companion.assertThat
import com.orangefox.ripple.consolidation.contract.model.*
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID.randomUUID

@ExtendWith(MockKExtension::class)
internal class RecurringContractTests {

  val contracts = mockk<Contracts>(relaxUnitFun = true)
  val dateProvider = mockk<DateProvider>()
  val domainEvents = mockk<DomainEvents>(relaxUnitFun = true)

  @Test
  fun `given current date is past contract's end date and contract is recurring then it should be extended`() {
    // arrange
    val contractId = randomUUID()
    val contract = ActiveContract(
      id = ContractId(contractId),
      contractor = Contractor(AgencyId(randomUUID())),
      consolidator = Consolidator(AgencyId(randomUUID()), NumberOfHaps(1)),
      code = ContractCode("Recurring Contract Test"),
      valueCap = ValueCap(BigDecimal(1000.0)),
      balance = Balance(BigDecimal(100.0)),
      period = ContractPeriod(LocalDate.now(), NumberOfDays(30)),
      policies = emptyList(),
      meta = setOf(Recurring(strategy = NumberOfCycles(cycles = 4, currentCycle = 1)))
    )
    every { contracts.findAll() } returns listOf(contract)
    every { dateProvider.getCurrentDate() } returns contract.endDate.plusDays(1)

    // act
    val events = ContractsManager(contracts, dateProvider).extendContracts()

    // assert
    assertThat(events).containsContractExtendedEvent(contractId)
  }

  @Test
  fun `given a recurring contract in its last cycle and current date is past contract's end date then it should be expired`() {
    // arrange
    val contractToRenew = randomUUID()
    val contract = ActiveContract(
      id = ContractId(contractToRenew),
      contractor = Contractor(AgencyId(randomUUID())),
      consolidator = Consolidator(AgencyId(randomUUID()), NumberOfHaps(1)),
      code = ContractCode("Recurring Contract Test"),
      valueCap = ValueCap(BigDecimal(1000.0)),
      balance = Balance(BigDecimal(100.0)),
      period = ContractPeriod(LocalDate.now(), NumberOfDays(30)),
      policies = emptyList(),
      meta = setOf(Recurring(strategy = NumberOfCycles(cycles = 4, currentCycle = 4)))
    )
    every { contracts.findAll() } returns listOf(contract)
    every { dateProvider.getCurrentDate() } returns contract.endDate.plusDays(1)

    // act
    val events = ContractsManager(contracts, dateProvider).expireContracts()

    // assert
    assertThat(events).containsContractExpiredEvent(contractToRenew).doesNotContainRenewedEvent(contractToRenew)
  }

}

