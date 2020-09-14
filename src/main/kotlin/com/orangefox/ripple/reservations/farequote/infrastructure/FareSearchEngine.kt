package com.orangefox.ripple.reservations.farequote.infrastructure

import com.orangefox.ripple.commons.models.Message
import com.orangefox.ripple.consolidation.agency.model.*
import com.orangefox.ripple.consolidation.policy.application.FareCalculator
import com.orangefox.ripple.consolidation.policy.application.FindPolicyById
import com.orangefox.ripple.consolidation.policy.model.Policy
import com.orangefox.ripple.consolidation.policy.model.Sector
import com.orangefox.ripple.reservations.contractor.model.ActiveContract
import com.orangefox.ripple.reservations.contractor.model.Contractor
import com.orangefox.ripple.reservations.contractor.model.Contractors
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.FareQuote
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.FareQuoteRepository
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.Segment
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.SegmentsRepository
import com.orangefox.ripple.reservations.farequote.infrastructure.gateway.FareQuoteMapper.mapToEntity
import com.orangefox.ripple.reservations.farequote.infrastructure.gateway.GatewayInterface
import com.orangefox.ripple.reservations.farequote.infrastructure.gateway.Request
import com.orangefox.ripple.reservations.farequote.infrastructure.gateway.Response
import com.orangefox.ripple.reservations.farequote.model.Fare
import com.orangefox.ripple.reservations.reservation.infrastructure.gateway.ServiceFailure
import com.orangefox.ripple.reservations.reservation.infrastructure.gateway.UAPIException
import mu.KLogging
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*
import com.orangefox.ripple.reservations.farequote.infrastructure.controller.search.Request as ControllerRequest

interface FareSearch {
  fun searchFares(request: ControllerRequest, contractor: AgencyId): List<AccessPointResponse>
}

@Component
class FareSearchEngine(
  private val contractors: Contractors,
  private val agencies: Agencies,
  private val policies: FindPolicyById,
  private val fareSearch: GatewayInterface,
  private val fareCalculator: FareCalculator,
  private val segmentsRepository: SegmentsRepository,
  private val fareQuoteRepository: FareQuoteRepository): FareSearch {

  override fun searchFares(request: ControllerRequest, contractor: AgencyId): List<AccessPointResponse> {

    val contractor = contractors.find(contractor)
      ?: throw IllegalStateException("Could not find Contractor with id ${contractor}")

    val fareQuotes = contractor.contracts.filterIsInstance<ActiveContract>()
      .flatMap { it to it.accessPoints }
      .map { retrieveAccessPoints(it) }
      .map { searchFareQuotes(it, contractor, request) } // TODO check blacklisted sectors
      .map { applyContractPolicies(it) }
      .flatMap { applyGeneralPolicies(contractor, it) }
      .also { responseList ->
        responseList.forEach { response ->
          val segments = segmentsRepository.saveAll(response.segments)
          fareQuoteRepository.saveAll(response.fareQuotes)
        }
      }

    return fareQuotes
  }

  private fun blockingSector(pair: Pair<ActiveContract, List<AccessPoint>>, sector: Sector): Boolean {
    val (contract, _) = pair
    if (contract.policies.isEmpty()) {
      return false
    }
    return contract.policies.map { policies.find(it) }
      .filterNotNull()
      .filter { it.blackList != null }
      .filter { it.blackList?.sectors?.contains(sector) ?: false }
      .isNotEmpty()
  }

  private fun searchFareQuotes(pair: Pair<ActiveContract, List<AccessPoint>>, contractor: Contractor, request: ControllerRequest): Pair<ActiveContract, List<AccessPointResponse>> {
    val (contract, accessPoints) = pair

    return contract to accessPoints.filterIsInstance<TravelportUAPI>()
      .map { api ->
        runCatching<Response> {
          fareSearch.searchFares(Request(api.credentials, request.legs, request.passengers))
        }.map {
          val segments = segmentsRepository.saveAll(it.segments).toList()
          AccessPointResponse(
            accessPoint = api.name,
            segments = it.segments,
            fareQuotes = it.fareQuotes.map { mapToEntity(it, segments, contract, contractor, api) },
            messages = it.messages
          )
        }.onFailure {
          when (it) {
            is ServiceFailure -> logger.error { "Internal service error from uapi-gateway. Message: ${it.message}" }
            is UAPIException -> logger.error {
              """
                | Received Error from UAPI Adapter
                | Error Code: ${it.code}
                | Description: ${it.message}
              """.trimMargin()
            }
          }
        }.getOrElse {
          AccessPointResponse(
            accessPoint = api.name,
            segments = emptyList(),
            fareQuotes = emptyList(),
            messages = listOf(
              Message(
                type = "error",
                message = """
                  An error occured while making request for fare quotes using access point ${api.name}
                  for contract with id ${contract.contractId}
                """.trimIndent()
              )
            )
          )
        }
      }
  }

  private fun retrieveAccessPoints(
    pair: Pair<ActiveContract, List<AccessPointId>>): Pair<ActiveContract, List<AccessPoint>> {

    val (contract, accessPointIds) = pair
    val consolidator = agencies.findAgency(contract.consolidator)
      ?: throw IllegalStateException("Could not find Contractor in Consolidation context")
    val accessPoints = accessPointIds.map { id ->
      consolidator.accessPoints.filter { it.id == id }.first()
    }
    return contract to accessPoints
  }

  private fun applyGeneralPolicies(contractor: Contractor, response: Pair<ActiveContract, List<AccessPointResponse>>): List<AccessPointResponse> {
    val (_, responseList) = response
    val policies = contractor.policies.map { policies.find(it) }.filterNotNull().filter { isApplicable(it) }
    if (policies.isNotEmpty()) {
      return responseList.map { response ->
        val modifiedQuotes = response.fareQuotes.map {
          val currency = Currency.getInstance(it.currencyCode)
          val modified = fareCalculator.calculateFare(
            Fare(it.basePrice, it.taxes, it.adjustment, currency),
            contractor.id
          )
          it.copy(adjustment = modified.adjustment)
        }
        response.copy(fareQuotes = modifiedQuotes)
      }
    }
    return responseList
  }

  private fun applyContractPolicies(response: Pair<ActiveContract, List<AccessPointResponse>>): Pair<ActiveContract, List<AccessPointResponse>> {
    val (contract, responseList) = response
    val policies = contract.policies.map { policies.find(it) }.filterNotNull().filter { isApplicable(it) }
    if (policies.isNotEmpty()) {
      return contract to responseList.map { response ->
        val modifiedQuotes = response.fareQuotes.map {
          val currency = Currency.getInstance(it.currencyCode)
          val modified = fareCalculator.calculateFare(
            Fare(it.basePrice, it.taxes, it.adjustment, currency),
            contract.id
          )
          it.copy(adjustment = modified.adjustment)
        }
        response.copy(fareQuotes = modifiedQuotes)
      }
    }
    return response
  }

  private fun isApplicable(policy: Policy): Boolean {
    val today = LocalDate.now()
    return today.isAfter(policy.startDate) && today.isBefore(policy.endDate)
  }

  companion object logging: KLogging()

}

private fun List<ActiveContract>.flatMap(transform: (ActiveContract) -> Pair<ActiveContract, List<AccessPointId>>): List<Pair<ActiveContract, List<AccessPointId>>> {
  return map { it to it.accessPoints }
}

data class AccessPointResponse(val accessPoint: AccessPointName, val segments: List<Segment>, val fareQuotes: List<FareQuote>, val messages: List<Message>)
