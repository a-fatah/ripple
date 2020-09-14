package com.orangefox.ripple.consolidation.server

import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component

@Component
class WebServerConfiguration: WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

  override fun customize(factory: ConfigurableWebServerFactory?) {
    factory?.setPort(8081)
  }

}
