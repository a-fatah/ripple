package com.orangefox.ripple.reservations.farequote.infrastructure.entity

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository
import java.util.*

@RepositoryRestResource
interface FareQuoteRepository: PagingAndSortingRepository<FareQuote, UUID>

@Repository
interface SegmentsRepository: CrudRepository<Segment, UUID> {
  fun findByKey(key: String): Optional<Segment>
}
