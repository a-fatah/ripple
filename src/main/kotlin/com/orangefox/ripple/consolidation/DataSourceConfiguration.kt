package com.orangefox.ripple.consolidation

import com.orangefox.ripple.commons.repositories.SpringSecurityAuditorAware
import com.orangefox.ripple.consolidation.agency.infrastructure.Agency
import com.orangefox.ripple.consolidation.contract.infrastructure.Contract
import com.orangefox.ripple.consolidation.policy.infrastructure.Policy
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.transaction.PlatformTransactionManager
import java.util.*

@Configuration
@EnableJdbcAuditing
open class DataSourceConfiguration: AbstractJdbcConfiguration() {

  @Bean
  open fun dataSource(): EmbeddedDatabase? {
    return EmbeddedDatabaseBuilder()
      .setName("consolidation")
      .addScript("consolidation.sql")
      .setType(EmbeddedDatabaseType.H2)
      .build()
  }

  @Bean
  open fun jdbcOperations(): NamedParameterJdbcOperations {
    return NamedParameterJdbcTemplate(dataSource())
  }

  @Bean
  open fun transactionManager(): PlatformTransactionManager {
    return DataSourceTransactionManager(dataSource())
  }

  @Bean
  open fun auditorProvider(): AuditorAware<String> {
    return SpringSecurityAuditorAware()
  }

  @Bean
  open fun idGenerator(): ApplicationListener<BeforeSaveEvent<Any>> {
    return object: ApplicationListener<BeforeSaveEvent<Any>> {
      override fun onApplicationEvent(event: BeforeSaveEvent<Any>) {
        when (event.entity) {
          is Agency -> {
            val agency = event.entity as Agency
            if (agency.id == null) {
              agency.id = UUID.randomUUID()
            }
          }
          is Contract -> {
            val contract = event.entity as Contract
            if (contract.id == null) {
              contract.id = UUID.randomUUID()
            }
          }
          is Policy -> {
            val policy = event.entity as Policy
            if (policy.id == null) {
              policy.id = UUID.randomUUID()
            }
          }
        }
      }
    }
  }

}
