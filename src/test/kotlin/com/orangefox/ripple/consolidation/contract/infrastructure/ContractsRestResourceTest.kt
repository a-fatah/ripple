package com.orangefox.ripple.consolidation.contract.infrastructure

import com.orangefox.ripple.consolidation.ConsolidationConfiguration
import com.orangefox.ripple.consolidation.agency.infrastructure.*
import com.orangefox.ripple.consolidation.agency.infrastructure.PolicyRef
import com.orangefox.ripple.consolidation.policy.infrastructure.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*


@ExtendWith(SpringExtension::class)
@WebMvcTest
@ContextConfiguration(classes = [ConsolidationConfiguration::class])
internal open class ContractsRestResourceTest {

  @Autowired
  lateinit var mockMvc: MockMvc
  @Autowired
  lateinit var policiesRepository: PolicyRepository
  @Autowired
  lateinit var agencyRepository: AgencyRepository

  lateinit var policyId: UUID
  lateinit var consolidatorId: UUID
  lateinit var contractorId: UUID
  lateinit var assignedAccessPoint: UUID

  @BeforeEach
  fun setup() {
    createPolicy()
    createConsolidator()
    createContractor()
  }

  @Test
  @WithMockUser
  fun `can save contract via HTTP POST`() {
    // arrange
    val json = """
      {
        "code": "TEST0001",
        "type": "DEBIT",
        "consolidator": {
          "uuid": "${consolidatorId}"
        },
        "contractor": {
          "uuid": "${contractorId}"
        },
        "startDate": "2020-03-20",
        "periodLength": 30,
        "valueCap": 100.0,
        "balance": 100.0,
        "locked": false,
        "settlementPeriod": 30,
        "recurrent": true,
        "numberOfCycles": 2,
        "currentCycle": 1,
        "allowDistribution": false,
        "fareReview": false,
        "allowVoid": true,
        "allowRefund": true,
        "allowExchange": true,
        "refundReview": false,
        "allowRevalidation": true,
        "voidCharges": 0,
        "refundCharges": 0,
        "exchangeCharges": 0,
        "allowTerminalAccess": true,
        "state": "${ContractState.DRAFT}",
        "accessPoints": [
          {
            "accessPoint": "${assignedAccessPoint}"
          }
        ]
      }

    """.trimIndent()

    // act
    mockMvc.post("/consolidation/contracts") {
      contentType = MediaType.APPLICATION_JSON_UTF8
      content = json
      with(csrf())
    }.andExpect {
      status {
        isCreated
      }
    }.andDo {
      print()
    }
  }

  private fun createPolicy() {
    // arrange
    val policy = Policy(
      startDate = LocalDate.now(),
      endDate = LocalDate.now().plusDays(30),
      commissions = setOf(
        Commission(
          name = "5% Commission",
          strategy = CalculationStrategy.FLAT,
          value = BigDecimal(5.0),
          bookingClass = "Y"
        )
      ),
      discounts = setOf(
        Discount(
          name = "5% Discount",
          strategy = CalculationStrategy.FLAT,
          value = BigDecimal(5.0),
          bookingClass = "Y"
        )
      ),
      blockedSectors = setOf(
        BlockedSector(name = "KHI-DXB", origin = "KHI", dest = "DXB")
      )
    )
    // act
    val saved = policiesRepository.save(policy)
    policyId = saved.id ?: throw IllegalStateException("Could not save policy")
  }

  private fun createConsolidator() {
    val consolidator = Agency(
      name = "ABC Travels",
      email = "test@gmail.com",
      phone = "031323234235",
      address = Address(
        street = "21 Commercial Street",
        city = "KHI",
        country = "PK"
      ),
      accessPoints = setOf(
        AccessPoint(
          name = "Galileo Access Point",
          provider = Provider.GALILEO,
          credentials = Credentials(
            type = CredentialType.HAP,
            username = "****",
            password = "****",
            targetBranch = "***"
          )
        )
      ),
      policies = setOf(PolicyRef(policyId))
    )
    val saved = agencyRepository.save(consolidator)
    consolidatorId = saved.id ?: throw IllegalStateException("Could not save contractor")
    assignedAccessPoint = saved.accessPoints.first().id!!
  }

  private fun createContractor() {
    val contractor = Agency(
      name = "ABC Travels",
      email = "test@gmail.com",
      phone = "031323234235",
      address = Address(
        street = "21 Commercial Street",
        city = "KHI",
        country = "PK"
      )
    )
    val saved = agencyRepository.save(contractor)
    contractorId = saved.id ?: throw IllegalStateException("Could not save contractor")
  }

}
