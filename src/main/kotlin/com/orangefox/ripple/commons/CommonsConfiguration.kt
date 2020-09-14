package com.orangefox.ripple.commons

import com.orangefox.ripple.app.config.Exported
import com.orangefox.ripple.app.config.ExportedConfiguration
import com.orangefox.ripple.app.events.publisher.DomainEvents
import com.orangefox.ripple.app.events.publisher.NativeApplicationEventPublisher
import com.orangefox.ripple.commons.repositories.CitiesDatabase
import com.orangefox.ripple.commons.repositories.SpringSecurityAuditorAware
import com.orangefox.ripple.commons.repositories.jdbc.CitiesRepository
import com.orangefox.ripple.commons.repositories.jdbc.CountriesRepository
import com.orangefox.ripple.consolidation.agency.model.Cities
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
@ComponentScan
@Import(
  DataSourceConfiguration::class,
  ExportedConfiguration::class,
  SecurityConfiguration::class
)
@ImportAutoConfiguration(
  ServletWebServerFactoryAutoConfiguration::class,
  HttpMessageConvertersAutoConfiguration::class,
  DispatcherServletAutoConfiguration::class,
  WebMvcAutoConfiguration::class,
  ConfigurationPropertiesAutoConfiguration::class,
  H2ConsoleAutoConfiguration::class,
  RepositoryRestMvcAutoConfiguration::class,
  JacksonAutoConfiguration::class,
  SecurityAutoConfiguration::class
)
@EnableJdbcRepositories("com.orangefox.ripple.commons.repositories.jdbc")
open class CommonsConfiguration(val context: ApplicationContext) {

  @Bean
  @Exported
  open fun userDetailService(): UserDetailsService {
    return RippleUserDetailsService()
  }

  @Bean
  @Exported
  open fun countries(entityRepository: CountriesRepository): com.orangefox.ripple.consolidation.agency.model.Countries {
    return com.orangefox.ripple.commons.repositories.CountriesDatabase(entityRepository)
  }

  @Bean
  @Exported
  open fun cities(entityRepository: CitiesRepository): Cities {
    return CitiesDatabase(entityRepository)
  }

  @Bean
  open fun auditorProvider(): AuditorAware<String> {
    return SpringSecurityAuditorAware()
  }

  @Bean
  @Exported
  open fun domainEvents(): DomainEvents {
    return NativeApplicationEventPublisher(context)
  }

}
