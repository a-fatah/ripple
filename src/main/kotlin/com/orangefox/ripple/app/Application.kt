package com.orangefox.ripple.app

import ConsolidationBanner
import MainBanner
import ReservationBanner
import com.orangefox.ripple.commons.CommonsConfiguration
import com.orangefox.ripple.consolidation.ConsolidationConfiguration
import com.orangefox.ripple.reservations.ReservationConfiguration
import org.springframework.boot.WebApplicationType
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Configuration


@Configuration
open class Application

fun main() {
  SpringApplicationBuilder()
    .parent(Application::class.java)
    .child(CommonsConfiguration::class.java).web(WebApplicationType.SERVLET).banner(MainBanner())
    .profiles("prod")
    .sibling(ConsolidationConfiguration::class.java).web(WebApplicationType.SERVLET).banner(ConsolidationBanner())
    .profiles("prod")
    .sibling(ReservationConfiguration::class.java).web(WebApplicationType.SERVLET).banner(ReservationBanner())
    .profiles("prod")
    .run()
}


