package com.orangefox.ripple.commons

import com.orangefox.ripple.commons.entities.Role
import com.orangefox.ripple.commons.entities.User
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*

@Configuration
@EnableWebSecurity
open class SecurityConfiguration: WebSecurityConfigurerAdapter() {

  override fun configure(http: HttpSecurity) {
    http.authorizeRequests().anyRequest().authenticated().and().formLogin().permitAll().and().logout().permitAll().and().csrf().disable()
  }

}

class RippleUserDetailsService: UserDetailsService {

  private val users: List<User> = listOf(
    User(userName = "admin", pass = "{noop}admin", agency = UUID.randomUUID(), roles = setOf(Role(name ="USER")), enabled = true)
  )
  override fun loadUserByUsername(username: String?): User {
    return runCatching {
      users.first { it.userName == username }
    }.getOrThrow()
  }
}

