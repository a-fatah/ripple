package com.orangefox.ripple.reservations

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.orangefox.ripple.consolidation.agency.infrastructure.AccessPoint
import com.orangefox.ripple.reservations.farequote.infrastructure.controller.reservation.ReservationController
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.FareQuote
import com.orangefox.ripple.reservations.reservation.infrastructure.UniversalRecord
import com.orangefox.ripple.reservations.reservation.infrastructure.gateway.UAPIGatewayErrorHandler
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL
import org.springframework.hateoas.server.RepresentationModelProcessor
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate

@Configuration
@EnableHypermediaSupport(type = [HAL])
open class RestConfiguration: RepositoryRestConfigurer {

  override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
    config.setBasePath("reservations/")
    config.exposeIdsFor(AccessPoint::class.java)
  }

  @Bean
  open fun restTemplate(): RestTemplate {
    return RestTemplateBuilder().errorHandler(restErrorHandler()).build()
  }

  @Bean
  open fun jacksonMapper(): ObjectMapper {
    return jacksonObjectMapper()
  }

  @Bean
  open fun restErrorHandler(): ResponseErrorHandler {
    return UAPIGatewayErrorHandler(jacksonMapper())
  }

  @Bean
  open fun fareQuoteProcessor(): RepresentationModelProcessor<EntityModel<FareQuote>> {
    return RepresentationModelProcessor<EntityModel<FareQuote>> { model ->
      val id = model.content.id!!

      model.add(
        linkTo<ReservationController> { makeReservation(id, null) }.withRel("reserve")
      )

    }
  }

  @Bean
  open fun universalRecordProcessor(): RepresentationModelProcessor<EntityModel<UniversalRecord>> {
    return RepresentationModelProcessor<EntityModel<UniversalRecord>> { model ->
      val id = model.content.id!!
      val notCancelled = !model.content.cancelled

      val self = model.links.getLink("self").get().href
      model.addIf(notCancelled) {
        Link.of(self + "/cancel", "cancel")
      }

      model.add(Link.of(self.toString() + "/reservations", "reservations"))
    }
  }
}
