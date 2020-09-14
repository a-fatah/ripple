package com.orangefox.ripple.reservations

import com.orangefox.ripple.reservations.contractor.infrastructure.Contractor
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.FareQuote
import com.orangefox.ripple.reservations.farequote.infrastructure.entity.Segment
import com.orangefox.ripple.reservations.reservation.infrastructure.Reservation
import com.orangefox.ripple.reservations.reservation.infrastructure.Traveler
import com.orangefox.ripple.reservations.reservation.infrastructure.UniversalRecord
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
      .setName("reservation")
      .addScript("reservation.sql")
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
  open fun auditorProvider(): AuditorAware<UUID> {
    return AgencyAuditor()
  }

  @Bean
  open fun idGenerator(): ApplicationListener<BeforeSaveEvent<Any>> {
    return object : ApplicationListener<BeforeSaveEvent<Any>> {
      override fun onApplicationEvent(event: BeforeSaveEvent<Any>) {
        when (event.entity) {
          is Contractor -> {
            val contractor = event.entity as Contractor
            if (contractor.id == null) {
              contractor.id = UUID.randomUUID()
            }
          }
          is FareQuote -> {
            val fareQuote = event.entity as FareQuote
            if (fareQuote.id == null) {
              fareQuote.id = UUID.randomUUID()
            }
          }
          is UniversalRecord -> {
            val universalRecord = event.entity as UniversalRecord
            if (universalRecord.id == null) {
              universalRecord.id = UUID.randomUUID()
            }
          }
          is Segment -> {
            val segment = event.entity as Segment
            if (segment.id == null) {
              segment.id = UUID.randomUUID()
            }
          }
          is Reservation -> {
            val reservation = event.entity as Reservation
            if (reservation.id == null) {
              reservation.id = UUID.randomUUID()
            }
          }
          is Traveler -> {
            val traveler = event.entity as Traveler
            if (traveler.id == null) {
              traveler.id = UUID.randomUUID()
            }
          }
        }
      }
    }
  }

}
