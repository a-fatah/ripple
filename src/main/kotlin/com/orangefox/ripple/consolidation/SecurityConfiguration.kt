package com.orangefox.ripple.consolidation

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class SecurityConfiguration: WebSecurityConfigurerAdapter() {

  @Bean
  @Profile("prod")
  open fun userDetailService(): UserDetailsService {
    return applicationContext.parent.getBean(UserDetailsService::class.java)
  }

  override fun configure(http: HttpSecurity) {
    http.headers().frameOptions().sameOrigin()
      .and().authorizeRequests()
      .antMatchers("/console/**").permitAll()

    http.authorizeRequests()
      .anyRequest().authenticated()
      .and()
      .formLogin().defaultSuccessUrl("/consolidation")
      .and()
      .logout().permitAll()
      .and()
      .csrf().disable()
  }

}
