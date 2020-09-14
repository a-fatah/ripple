package com.orangefox.ripple.reservations.contractor.infrastructure

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.util.*

@RepositoryRestResource
interface ContractorRepository: CrudRepository<Contractor, UUID> {

  @Query("SELECT contractor FROM Contract WHERE id = :contractId")
  fun findContractor(contractId: UUID): UUID

}
