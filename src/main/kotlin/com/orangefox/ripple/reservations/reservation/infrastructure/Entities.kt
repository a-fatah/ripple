package com.orangefox.ripple.reservations.reservation.infrastructure

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*


data class UniversalRecord(
  val locatorCode: String,
  val reservations: Set<ReservationRef>,
  val travelers: Set<TravelerRef>,
  val cancelled: Boolean = false,
  val version: String,
  val status: String,
  val accessPoint: UUID,
  val contract: UUID
) {
  @Id var id: UUID? = null
}

data class Reservation(
  val locatorCode: String,
  val providerCode: String,
  val providerLocatorCode: String,
  val supplierCode: String,
  val supplierLocatorCode: String,
  val createdDate: String,
  val modifiedDate: String,
  val cancelled: Boolean = false,
  val segments: Set<SegmentRef>
) {
  @Id var id: UUID? = null
}

data class ReservationRef(val reservation: UUID) {
  @Id @Column("UNIVERSAL_RECORD") var id: UUID? = null
}

data class TravelerRef(val traveler: UUID) {
  @Id @Column("UNIVERSAL_RECORD") var id: UUID? = null
}

@Table("RESERVATION_SEGMENT_REF")
data class SegmentRef(val segment: UUID) {
  @Id @Column("RESERVATION") var id: UUID? = null
}

data class Traveler(
  val key: String?,
  val prefix: String,
  val firstName: String,
  val lastName: String,
  val dob: String?,
  val gender: String?,
  val type: String,
  val phone: Phone,
  val email: Email
) {
  @Id var id: UUID? = null
}

data class Phone(
  val countryCode: String,
  val phoneNumber: String
) {
  @Id var id: Long? = null
}

data class Email(
  val type: String,
  val emailId: String
) {
  @Id var id: Long? = null
}
