package com.orangefox.ripple.commons.repositories

import com.orangefox.ripple.commons.entities.User
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*


class SpringSecurityAuditorAware: AuditorAware<String> {

  override fun getCurrentAuditor(): Optional<String> {
    return Optional.ofNullable(SecurityContextHolder.getContext())
      .map { it.authentication }
      .filter { it.isAuthenticated }
      .map { it.principal }
      .map { it as User }
      .map { it.username }
  }

}
