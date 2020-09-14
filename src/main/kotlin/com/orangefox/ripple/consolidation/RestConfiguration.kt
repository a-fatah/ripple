package com.orangefox.ripple.consolidation

import com.orangefox.ripple.consolidation.agency.infrastructure.AccessPoint
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer

@Configuration
open class RestConfiguration: RepositoryRestConfigurer {
  override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration?) {
    config?.setBasePath("/consolidation/")
    config?.exposeIdsFor(AccessPoint::class.java)
  }

}
