package com.orangefox.ripple.consolidation.policy.infrastructure.rest

import com.orangefox.ripple.consolidation.ConsolidationConfiguration
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


@ExtendWith(SpringExtension::class)
@WebMvcTest
@ContextConfiguration(classes = [ConsolidationConfiguration::class])
internal open class PolicyResourceTest {

  @Autowired lateinit var mockMvc: MockMvc

  @Test
  @WithMockUser
  fun `creates general policy via HTTP POST`() {
    // arrange
    val json = """
    {
       "startDate": "2020-03-16",
       "endDate": "2020-04-16",
       "commissions":[
          {
             "name":"Flat 5% Commission",
             "bookingClass": "Y",
             "strategy":"FLAT",
             "value":5
          }
       ],
       "discounts":[
          {
             "name":"Flat 5% Discount",
             "bookingClass": "Y",
             "strategy":"FLAT",
             "value":5
          }
       ],
       "allowedAirlines":[
          {
             "airline":"PK"
          }
       ],
       "blockedSectors":[
          {
             "name":"Block KHI-DXB",
             "origin":"KHI",
             "dest":"DXB"
          }
       ],
       "blockedAirlines":[
          {
             "name":"Block PK",
             "airline":"PK"
          }
       ],
       "blockedSectorsOnAirlines":[
          {
             "name":"Block KHI-DXB on PK",
             "origin":"KHI",
             "dest":"DXB",
             "airline":"PK"
          }
       ]
    }
    """

    // act
    mockMvc.post("/consolidation/policies/") {
      contentType = MediaType.APPLICATION_JSON_UTF8
      content = json
      with(csrf())
    }.andExpect {
      status {
        isCreated
      }
    }
  }

}
