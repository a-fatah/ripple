package com.orangefox.ripple.reservations

import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component

@Component
open class WebServerCustomizer: WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

  override fun customize(factory: ConfigurableWebServerFactory) {
    factory.setPort(8082)
  }

}
