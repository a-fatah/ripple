package com.orangefox.ripple.consolidation.policy.application

import com.orangefox.ripple.consolidation.policy.infrastructure.DomainMapper.mapToDomain
import com.orangefox.ripple.consolidation.policy.infrastructure.PolicyRepository
import com.orangefox.ripple.consolidation.policy.model.Policy
import com.orangefox.ripple.consolidation.policy.model.PolicyId
import org.springframework.stereotype.Component

@Component
class PolicyRepository(private val repository: PolicyRepository): FindPolicyById {

  override fun find(policy: PolicyId): Policy? {
    val entity = repository.findById(policy.id)
    return entity.map { mapToDomain(it) }.orElse(null)
  }

}

interface FindPolicyById {
  fun find(policyId: PolicyId): Policy?
}
