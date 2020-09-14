package com.orangefox.ripple.consolidation.contract.infrastructure

import com.orangefox.ripple.commons.CommonsConfiguration
import com.orangefox.ripple.consolidation.ConsolidationConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.ContextHierarchy
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID.randomUUID

@ExtendWith(SpringExtension::class)
@ContextHierarchy(
  ContextConfiguration(classes = [CommonsConfiguration::class]),
  ContextConfiguration(classes = [ConsolidationConfiguration::class])
)
internal open class ContractJdbcRepositoryTest {

  @Autowired
  lateinit var contractRepository: ContractsRepository

  @Test
  fun `can save contract to database`() {
    //arrange
    var contract = Contract(
      type = ContractType.DEBIT,
      code = "TEST0001",
      consolidator = ConsolidatorRef(uuid = randomUUID()),
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
      policies = mutableSetOf(PolicyRef(randomUUID()))
    )
    // act
    contract = contractRepository.save(contract)
    // assert
    assertThat(contract.id).isNotNull()
  }

  @Test
  fun `can calculate total of all transactions for a contract`() {
    // arrange
    var contract = Contract(
      type = ContractType.DEBIT,
      code = "TEST0001",
      consolidator = ConsolidatorRef(uuid = randomUUID()),
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
    // act
    contract = contractRepository.save(contract)
    val sum = contractRepository.getTransactionsTotalByContract(contract.id!!)
    // assert
    assertThat(sum).isEqualTo(BigDecimal.valueOf(300.0))
  }

}
