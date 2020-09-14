package com.orangefox.ripple.consolidation.contract.infrastructure

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.security.access.prepost.PostFilter
import java.math.BigDecimal
import java.util.*

@RepositoryRestResource
interface ContractsRepository: CrudRepository<Contract, UUID> {

  @Query("SELECT SUM(amount) FROM contract_transaction t WHERE t.contract = :contractId")
  fun getTransactionsTotalByContract(contractId: UUID): BigDecimal

  @Query("SELECT SUM(amount) FROM payment p WHERE P.CONTRACT = :contractId")
  fun getPaymentTotalByContract(contractId: UUID): BigDecimal

  @Query("SELECT * FROM CONTRACT_CONTRACTOR")
  @PostFilter("filterObject.uuid == authentication.principal.agency")
  fun findContractsForContractor(): Iterable<ContractorRef>

  @Query("SELECT * FROM CONTRACT_CONSOLIDATOR")
  @PostFilter("filterObject.uuid == authentication.principal.agency")
  fun findContractsForConsolidator(): Iterable<ConsolidatorRef>

}
