package com.orangefox.ripple.reservations.reservation.infrastructure.resource

import com.orangefox.ripple.reservations.reservation.infrastructure.UniversalRecord
import com.orangefox.ripple.reservations.reservation.infrastructure.controller.UniversalRecordController
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.linkTo

class UniversalRecordAssembler: RepresentationModelAssembler<UniversalRecord, EntityModel<UniversalRecord>> {

  override fun toModel(entity: UniversalRecord): EntityModel<UniversalRecord> {
    val model = EntityModel.of(entity)
    model.add(
      linkTo<UniversalRecordController> { listReservations(entity.id!!) }.withRel("reservations")
    )
    // add links for reservations, travelers, access_point, contract
    return model
  }
}
