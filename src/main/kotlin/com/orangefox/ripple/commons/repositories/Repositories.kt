package com.orangefox.ripple.commons.repositories

import com.orangefox.ripple.commons.models.*
import com.orangefox.ripple.commons.repositories.jdbc.CitiesRepository
import com.orangefox.ripple.commons.repositories.jdbc.CountriesRepository
import com.orangefox.ripple.consolidation.agency.model.Cities
import com.orangefox.ripple.consolidation.agency.model.Countries
import org.springframework.beans.factory.annotation.Autowired
import com.orangefox.ripple.commons.entities.City as CityEntity
import com.orangefox.ripple.commons.entities.Country as CountryEntity

class CountriesDatabase @Autowired constructor(
  private val countryRepository: CountriesRepository
) : Countries {
  override fun findAll(): List<Country> {
    return countryRepository.findAll().map { mapToDomain(it) }
  }

  override fun findCountry(country: CountryCode): Country? {
    val entity = countryRepository.findByCode(country.code)
    return entity?.let { mapToDomain(it) }
  }

  private fun mapToDomain(it: CountryEntity) = Country(CountryCode(it.code), CountryName(it.name))
}

class CitiesDatabase @Autowired constructor(
  private val cityRepository: CitiesRepository
): Cities {

  override fun findCity(city: CityCode): City? {
    return cityRepository.findByCode(city.code)?.let { mapToDomain(it) }
  }

  private fun mapToDomain(city: CityEntity) = City(CityCode(city.code), CityName(city.name))
}

