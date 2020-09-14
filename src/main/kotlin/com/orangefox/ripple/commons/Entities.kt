package com.orangefox.ripple.commons.entities

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

abstract class  Entity<T, ID>(
  @Transient val _id: ID?,
  @Transient var newEntity: Boolean = false): Persistable<ID> {
  override fun isNew(): Boolean = newEntity
  override fun getId(): ID? = _id
  fun isNew(newEntity: Boolean): T {
    this.newEntity = newEntity
    return this as T
  }
}

data class Country(var code: String, var name:String) {
  @Id var id: Long? = null
}

data class City(var code: String, var name: String) {
  @Id var id: Long? = null
}

data class Airport(var code: String, var name: String) {
  @Id var id: Long? = null
}

data class Airline(var code: String, var name: String) {
  @Id var id: Long? = null
}

data class User(
  @Id var id: UUID? = null,
  var userName: String,
  var pass: String,
  var agency: UUID,
  var roles: Set<Role> = emptySet(),
  var enabled: Boolean,
  var allowRefund: Boolean = true,
  var allowVoid: Boolean = true
): UserDetails, Entity<User, UUID>(id, true) {

  override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
    return roles.map { SimpleGrantedAuthority(it?.name) }.toMutableSet()
  }

  override fun getUsername(): String {
    return userName
  }

  override fun getPassword(): String {
    return pass
  }

  override fun isAccountNonExpired(): Boolean {
    return true
  }

  override fun isCredentialsNonExpired(): Boolean {
    return true
  }

  override fun isEnabled(): Boolean {
    return enabled
  }

  override fun isAccountNonLocked(): Boolean {
    return true
  }
}

data class Role(@Id @Column("USER") val id: String? = null, var name: String)
