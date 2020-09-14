package com.orangefox.ripple.reservations.farequote.infrastructure.controller.search

import com.orangefox.ripple.commons.models.Leg
import com.orangefox.ripple.commons.models.Passenger
import com.orangefox.ripple.consolidation.agency.model.AgencyId
import com.orangefox.ripple.reservations.farequote.infrastructure.AccessPointResponse
import com.orangefox.ripple.reservations.farequote.infrastructure.FareSearch
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
open class FareSearchController(val searchEngine: FareSearch) {

  @PostMapping("reservations/contractors/{id}/fareQuotes")
  open fun getFareQuotes(@PathVariable("id") contractor: UUID, @RequestBody request: Request?): Response {
    val responses = searchEngine.searchFares(request!!, AgencyId(contractor))
    return Response(responses)
  }

}

class Request(
  val legs: List<Leg>,
  val passengers: List<Passenger>
)

data class Response(val results: List<AccessPointResponse>)
