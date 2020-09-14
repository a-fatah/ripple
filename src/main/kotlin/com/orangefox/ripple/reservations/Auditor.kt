package com.orangefox.ripple.reservations

import com.orangefox.ripple.commons.entities.User
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

class AgencyAuditor: AuditorAware<UUID> {

  override fun getCurrentAuditor(): Optional<UUID> {
    return Optional.ofNullable(SecurityContextHolder.getContext())
      .map { it.authentication }
      .filter { it.isAuthenticated }
      .map { it.principal }
      .map { it as User }
      .map { it.agency }
  }

}
