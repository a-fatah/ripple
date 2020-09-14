package com.orangefox.ripple.consolidation.policy.infrastructure

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository("policyCrudRepository")
interface PolicyRepository: CrudRepository<Policy, UUID>
