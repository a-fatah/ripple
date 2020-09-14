package com.orangefox.ripple.commons.repositories.jdbc

import com.orangefox.ripple.commons.entities.*
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource


@RepositoryRestResource
interface CountriesRepository: PagingAndSortingRepository<Country, Long> {
  @Query("SELECT * FROM COUNTRY WHERE code = :code")
  fun findByCode(@Param("code") code: String): Country?
}

@RepositoryRestResource
interface CitiesRepository: PagingAndSortingRepository<City, Long> {
  @Query("SELECT code, name FROM CITY c WHERE c.code = :code")
  fun findByCode(@Param("code") code: String): City?
}

@RepositoryRestResource
interface AirlinesRepository: PagingAndSortingRepository<Airline, Long> {
  @Query("SELECT code, name FROM Airline a WHERE a.code = :code")
  fun findByCode(@Param("code") code: String): Airline?
}

@RepositoryRestResource
interface AirportsRepository : PagingAndSortingRepository<Airport, Long> {
  @Query("SELECT code, name FROM Airport a WHERE a.code = :code")
  fun findByCode(@Param("code") code: String): Airport?
}

@RepositoryRestResource(exported = false)
interface UserRepository: CrudRepository<User, String>
