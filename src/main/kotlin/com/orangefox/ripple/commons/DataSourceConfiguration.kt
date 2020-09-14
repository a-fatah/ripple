package com.orangefox.ripple.commons

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@EnableJdbcAuditing
open class DataSourceConfiguration: AbstractJdbcConfiguration() {

  @Bean
  open fun dataSource(): EmbeddedDatabase? {
    return EmbeddedDatabaseBuilder()
      .setName("commons")
      .addScript("commons.sql")
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

}
