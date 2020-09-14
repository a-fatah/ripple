package com.orangefox.ripple.consolidation.contract.model

import com.orangefox.ripple.consolidation.agency.model.AgencyId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.LocalDate
import java.util.UUID.randomUUID

internal open class ContractCreationTests {

  @Test
  fun `given consolidator has sufficient balance then it should activate the credit contract`() {
    // arrange
    val contract = ContractDraft(
      id = ContractId(randomUUID()),
      consolidator = Consolidator(AgencyId(randomUUID())),
      contractor = Contractor(AgencyId(randomUUID())),
      code = ContractCode("TEST"),
      period = ContractPeriod(LocalDate.now(), NumberOfDays(30)),
      haps = NumberOfHaps(1),
      valueCap = ValueCap(BigDecimal(100.0)),
      meta = setOf(CreditContract())
    )

    // act
    val result = contract.validate()

    // assert
    assertThat(result).isInstanceOf(ContractActivated::class.java)
  }

  @Test
  fun `given no payment received when validate contract draft created then it is Suspended`(){
    // arrange
    val contract = ContractDraft(
      id = ContractId(randomUUID()),
      consolidator = Consolidator(agency = AgencyId(randomUUID())),
      contractor = Contractor(agency = AgencyId(randomUUID())),
      valueCap = ValueCap(BigDecimal(100.0)),
      code = ContractCode("TEST"),
      haps = NumberOfHaps(1),
      meta = setOf(DebitContract())
    )

    // act
    val result = contract.validate()

    // assert
    assertThat(result).isInstanceOf(ContractSuspended::class.java)
    assertThat((result as ContractSuspended).reason)
      .isEqualTo("Contract suspended due to missing payment")
  }

  @Test
  fun `given consolidator has no HAPs (access points) when validate contract draft then it is Invalidated`() {
    // arrange
    val contract = ContractDraft(
      id = ContractId(randomUUID()),
      consolidator = Consolidator(AgencyId(randomUUID()), NumberOfHaps(0)),
      contractor = Contractor(AgencyId(randomUUID())),
      code = ContractCode("TEST"),
      valueCap = ValueCap(BigDecimal(100.0)),
      haps = NumberOfHaps(1)
    )

    // act
    val result = contract.validate()

    // assert
    assertThat(result).isInstanceOf(ContractInvalidated::class.java)
    assertThat((result as ContractInvalidated).reason).isEqualTo("Consolidator does not have access points")
  }

  @Test
  fun `given parent contract does not allow distribution when validate child contract draft then it is Invalidated`() {
    // arrange
    val contractId = ContractId(randomUUID())
    val parentId = ContractId(randomUUID())
    val consolidatorId = AgencyId(randomUUID())
    val contractorId = AgencyId(randomUUID())

    val consolidator = Consolidator(consolidatorId, NumberOfHaps(1))
    val contractor = Contractor(contractorId)
    val parentContract = ActiveContract(
      id = parentId,
      code = ContractCode("TEST01"),
      consolidator = consolidator,
      contractor = contractor,
      valueCap = ValueCap(BigDecimal(1000.0)),
      policies = emptyList(),
      meta = setOf(DebitContract())
    )

    val contract = ChildContractDraft(
      id = contractId,
      parent = parentContract,
      consolidator = Consolidator(consolidatorId),
      contractor = Contractor(contractorId),
      code = ContractCode("TEST"),
      valueCap = ValueCap(BigDecimal(500.0)),
      haps = NumberOfHaps(1),
      meta = setOf(Child(parent = parentContract), DebitContract())
    )

    // act
    val result = contract.validate()

    // assert
    assertThat(result).isInstanceOf(ContractInvalidated::class.java)
    assertThat((result as ContractInvalidated).reason).isEqualTo("Parent contract does not allow distribution")
  }

  @Test
  fun `given parent contract is maxed out when validate child contract draft then it is Invalidated`() {
    // arrange
    val contractId = ContractId(randomUUID())
    val parentId = ContractId(randomUUID())
    val consolidatorId = AgencyId(randomUUID())
    val contractorId = AgencyId(randomUUID())

    val consolidator = Consolidator(consolidatorId, NumberOfHaps(1))
    val contractor = Contractor(contractorId)
    val parentContract = ActiveContract(
      id = parentId,
      code = ContractCode("TEST01"),
      consolidator = consolidator,
      contractor = contractor,
      valueCap = ValueCap(BigDecimal(100.0)),
      balance = Balance(ZERO),
      policies = emptyList(),
      meta = setOf(Descendible())
    )

    val contract = ChildContractDraft(
      id = contractId,
      parent = parentContract,
      consolidator = Consolidator(consolidatorId),
      contractor = Contractor(contractorId),
      code = ContractCode("TEST"),
      valueCap = ValueCap(BigDecimal(500.0)),
      haps = NumberOfHaps(1),
      meta = setOf(Child(parent = parentContract))
    )

    // act
    val result = contract.validate()

    // assert
    assertThat(result).isInstanceOf(ContractInvalidated::class.java)
    assertThat((result as ContractInvalidated).reason)
      .isEqualTo("Value cap should be less than or equal to parent contract's available limit")
  }

}
