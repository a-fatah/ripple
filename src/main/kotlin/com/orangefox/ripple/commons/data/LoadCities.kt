package com.orangefox.ripple.commons.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.orangefox.ripple.commons.entities.City
import com.orangefox.ripple.commons.repositories.jdbc.CitiesRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(value = ["seed.cities"], havingValue = "true", matchIfMissing = false)
class LoadCities(
  private val objectMapper: ObjectMapper,
  private val cities: CitiesRepository
): CommandLineRunner {
  override fun run(vararg args: String?) {
    val resource = ClassPathResource("cities.json")
    if (resource.exists()) {
      var citiesList = objectMapper.readValue(resource.file, CitiesList::class.java)
      cities.saveAll(citiesList.cities.map {
        City(it.code, it.name)
      })
      println("Loaded ${citiesList.count} cities!")
    } else {
      println("cities.json file not found in classpath")
    }
  }

  data class CitiesList(val cities: List<City>, val count: Int)

}
