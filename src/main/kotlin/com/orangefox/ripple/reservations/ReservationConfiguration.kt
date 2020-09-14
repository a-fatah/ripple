package com.orangefox.ripple.reservations

import com.orangefox.ripple.reservations.contractor.application.ContractorRepository
import com.orangefox.ripple.reservations.contractor.model.Contractors
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import java.io.PrintStream
import com.orangefox.ripple.reservations.contractor.infrastructure.ContractorRepository as ContractorJdbcRepository

@Configuration
@ComponentScan(basePackages = [
  "com.orangefox.ripple.reservations.contractor.application",
  "com.orangefox.ripple.reservations.contractor.infrastructure",
  "com.orangefox.ripple.reservations.farequote.infrastructure",
  "com.orangefox.ripple.reservations.reservation.infrastructure"
])
@EnableJdbcRepositories(
  "com.orangefox.ripple.reservations.contractor.infrastructure",
  "com.orangefox.ripple.reservations.farequote.infrastructure",
  "com.orangefox.ripple.reservations.reservation.infrastructure"
)
@EnableScheduling
@ImportAutoConfiguration(
  ServletWebServerFactoryAutoConfiguration::class,
  HttpMessageConvertersAutoConfiguration::class,
  DispatcherServletAutoConfiguration::class,
  WebMvcAutoConfiguration::class,
  ConfigurationPropertiesAutoConfiguration::class,
  H2ConsoleAutoConfiguration::class,
  RepositoryRestMvcAutoConfiguration::class,
  JacksonAutoConfiguration::class
)
@Import(
  DataSourceConfiguration::class,
  WebServerCustomizer::class,
  RestConfiguration::class,
  SecurityConfiguration::class,
  DomainEventsConfiguration::class
)
open class ReservationConfiguration {

  @Bean
  open fun contractors(jdbcRepository: ContractorJdbcRepository): Contractors {
    return ContractorRepository(jdbcRepository)
  }

}
