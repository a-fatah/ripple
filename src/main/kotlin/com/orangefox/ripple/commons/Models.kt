package com.orangefox.ripple.commons.models

import java.net.URL

class Country(val code: CountryCode, val name: CountryName) {
  class Builder {
    lateinit var code: CountryCode
    lateinit var name: CountryName

    fun name(name: String) {
      this.name = CountryName(name)
    }

    fun code(code: String) {
      this.code = CountryCode(code)
    }

    fun build(block: Builder.() -> Unit): Country {
      block()
      return Country(code, name)
    }
  }
}
class CountryCode(val code: String)
class CountryName(val name: String)

class City(val code: CityCode, val name: CityName) {
  class Builder {
    lateinit var code: CityCode
    lateinit var name: CityName

    fun name(name: String) {
      this.name = CityName(name)
    }

    fun code(code: String) {
      this.code = CityCode(code)
    }

    fun build(block: Builder.() -> Unit): City {
      block()
      return City(code, name)
    }
  }
}

class CityCode(val code: String)
class CityName(val name: String)

data class Airline(val code: AirlineCode, val name: AirlineName, val logo: AirlineLogo?=null) {
  class Builder {
    lateinit var code: AirlineCode
    lateinit var name: AirlineName

    fun build(block: Builder.() -> Unit): Airline {
      block()
      return Airline(code, name)
    }
  }
}
data class AirlineCode(val code: String)
data class AirlineName(val name: String)
data class AirlineLogo(val url: URL)

data class Sector(val origin: Airport, val destination: Airport)

data class Airport(val code: AirportCode, val name: AirportName) {

  class Builder {
    lateinit var code: AirportCode
    lateinit var name: AirportName

    fun build(block: Builder.() -> Unit): Airport {
      block()
      return Airport(code, name)
    }
  }
}
data class AirportCode(val code: String)
data class AirportName(val name: String)

fun airport(block: Airport.Builder.() -> Unit): Airport = Airport.Builder().build(block)
fun airline(block: Airline.Builder.() -> Unit): Airline = Airline.Builder().build(block)
fun city(block: City.Builder.() -> Unit): City = City.Builder().build(block)
fun country(block: Country.Builder.() -> Unit): Country = Country.Builder().build(block)

open class PhoneNumber(val number: String)
class Telephone(val n: String): PhoneNumber(n)
class Mobile(val n: String): PhoneNumber(n)
class Email(val email: String)

class Passenger(val type: String, val count: Int)
class Leg(val origin: String, val destination: String, val departureDate: String)
data class Message(val message: String, val type: String?)
