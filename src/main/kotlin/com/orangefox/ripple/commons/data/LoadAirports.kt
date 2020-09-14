package com.orangefox.ripple.commons.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.orangefox.ripple.commons.entities.Airport
import com.orangefox.ripple.commons.repositories.jdbc.AirportsRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(value = ["seed.airports"], havingValue = "true", matchIfMissing = false)
class LoadAirports(
        private val airports: AirportsRepository,
        private val objectMapper: ObjectMapper
): CommandLineRunner {

  override fun run(vararg args: String?) {
    val resource = ClassPathResource("airports.json")
    if (resource.exists()) {
      val airportsList = objectMapper.readValue(resource.file, Airports::class.java)
      airports.saveAll(
        airportsList.airports.map {
            Airport(it.code, it.name)
      })
      println("Loaded ${airportsList.count} airports!")
    } else {
      println("airports.json file not found in classpath")
    }

  }

  data class Airports(val airports: List<Airport>, val count: Int)

}
