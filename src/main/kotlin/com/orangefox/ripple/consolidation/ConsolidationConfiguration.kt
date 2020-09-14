package com.orangefox.ripple.consolidation

import com.orangefox.ripple.app.config.Exported
import com.orangefox.ripple.app.config.ExportedConfiguration
import com.orangefox.ripple.app.events.publisher.DomainEvents
import com.orangefox.ripple.app.events.publisher.NativeApplicationEventPublisher
import com.orangefox.ripple.consolidation.agency.application.AgencyRepository
import com.orangefox.ripple.consolidation.agency.infrastructure.AccessPointRepository
import com.orangefox.ripple.consolidation.agency.model.Agencies
import com.orangefox.ripple.consolidation.contract.application.ContractDatabaseRepository
import com.orangefox.ripple.consolidation.contract.infrastructure.ContractsRepository
import com.orangefox.ripple.consolidation.contract.model.Contracts
import com.orangefox.ripple.consolidation.policy.application.FareCalculator
import com.orangefox.ripple.consolidation.policy.application.FindPolicyById
import com.orangefox.ripple.consolidation.policy.application.PolicyRepository
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
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import java.io.PrintStream
import com.orangefox.ripple.consolidation.agency.infrastructure.AgencyRepository as AgencyCrudRepository
import com.orangefox.ripple.consolidation.agency.infrastructure.DomainMapper as AgencyDomainMapper
import com.orangefox.ripple.consolidation.contract.infrastructure.DomainMapper as ContractDomainMapper
import com.orangefox.ripple.consolidation.policy.infrastructure.PolicyRepository as PolicyCrudRepository

@Configuration
@EnableJdbcRepositories(basePackages = [
  "com.orangefox.ripple.consolidation.agency.infrastructure",
  "com.orangefox.ripple.consolidation.contract.infrastructure",
  "com.orangefox.ripple.consolidation.policy.infrastructure"
])
@ComponentScan(basePackages = [
  "com.orangefox.ripple.consolidation.agency.infrastructure",
  "com.orangefox.ripple.consolidation.contract.application",
  "com.orangefox.ripple.consolidation.contract.infrastructure",
  "com.orangefox.ripple.consolidation.policy.infrastructure",
  "com.orangefox.ripple.consolidation.policy.application",
  "com.orangefox.ripple.consolidation.server"
])
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
  RestConfiguration::class,
  ExportedConfiguration::class,
  SecurityConfiguration::class
)
open class ConsolidationConfiguration(val context: ApplicationContext) {

  @Bean
  @Exported
  open fun agencies(agencyJdbcRepo: AgencyCrudRepository, accessPointRepo: AccessPointRepository, domainMapper: AgencyDomainMapper): Agencies {
    return AgencyRepository(agencyJdbcRepo, accessPointRepo, domainMapper)
  }

  @Bean
  @Exported
  open fun contracts(contracts: ContractsRepository, domainEvents: DomainEvents, domainMapper: ContractDomainMapper): Contracts {
    return ContractDatabaseRepository(contracts, domainEvents, domainMapper)
  }

  @Bean
  @Exported
  open fun policies(repository: PolicyCrudRepository): FindPolicyById {
    return PolicyRepository(repository)
  }

  @Bean
  @Exported
  open fun fareCalculator(contracts: Contracts, agencies: Agencies): FareCalculator {
    return FareCalculator(contracts, agencies)
  }

  @Bean
  open fun domainEvents(): DomainEvents {
    return NativeApplicationEventPublisher(context)
  }

}
