package com.orangefox.ripple.consolidation.agency.infrastructure

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AgencyRepository: CrudRepository<Agency, UUID> {

  @Query("SELECT c.consolidator FROM Contract c INNER JOIN Agency a ON c.contractor = :contractorId")
  fun findConsolidators(@Param("contractor") contractorId: UUID): List<Agency>

  @Query("SELECT c.contractor FROM Contract c INNER JOIN Agency a on c.consolidator = :consolidatorId")
  fun findContractors(@Param("consolidator") consolidatorId: UUID): List<Agency>

}

@Repository
interface AccessPointRepository: CrudRepository<AccessPoint, UUID>
