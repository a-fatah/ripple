package com.orangefox.ripple.commons

import com.orangefox.ripple.commons.entities.Airport
import com.orangefox.ripple.commons.entities.City
import com.orangefox.ripple.commons.entities.Country
import com.orangefox.ripple.commons.repositories.jdbc.AirportsRepository
import com.orangefox.ripple.commons.repositories.jdbc.CitiesRepository
import com.orangefox.ripple.commons.repositories.jdbc.CountriesRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig


@SpringJUnitConfig(CommonsConfiguration::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class ReferenceDataSearchByCode(
  val countries: CountriesRepository,
  val cities: CitiesRepository,
  val airports: AirportsRepository
) {

  @Test
  fun `can find country by code`() {
    countries.save(Country("PK", "Pakistan"))
    assertNotNull(countries.findByCode("PK"))
  }

  @Test
  fun `can find city by code`() {
    cities.save(City("KHI", "Karachi"))
    assertNotNull(cities.findByCode("KHI"))
  }

  @Test
  fun `can find airport by code`() {
    airports.save(Airport("KHI", "Jinnah International Airport"))
    assertNotNull(airports.findByCode("KHI"))
  }
}
