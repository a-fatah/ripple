package com.orangefox.ripple.commons.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.orangefox.ripple.commons.entities.Country
import com.orangefox.ripple.commons.repositories.jdbc.CountriesRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(value = ["seed.countries"], havingValue = "true", matchIfMissing = false)
class LoadCountries(
        private val objectMapper: ObjectMapper,
        private val countries: CountriesRepository
): CommandLineRunner {
  override fun run(vararg args: String?) {
    val resource = ClassPathResource("countries.json")
    if (resource.exists()) {
      var countriesList = objectMapper.readValue(resource.file, CountriesList::class.java)
      countries.saveAll(countriesList.countries.map {
          Country(it.code, it.name)
      })
      println("Loaded ${countriesList.count} countries!")
    } else {
      println("countries.json file not found in the classpath")
    }
  }

  data class CountriesList(val countries: List<Country>, val count: Int)

}
