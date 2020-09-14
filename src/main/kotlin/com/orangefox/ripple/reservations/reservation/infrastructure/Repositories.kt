package com.orangefox.ripple.reservations.reservation.infrastructure

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.util.*

@RepositoryRestResource
interface UniversalRecordRepository: CrudRepository<UniversalRecord, UUID>

@RepositoryRestResource
interface ReservationRepository: CrudRepository<Reservation, UUID>

@RepositoryRestResource
interface TravelerRepository: CrudRepository<Traveler, UUID>
