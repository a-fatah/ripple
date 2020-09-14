package com.orangefox.ripple.consolidation.contract.model

import com.orangefox.ripple.app.events.publisher.DomainEvent
import com.orangefox.ripple.reservations.farequote.model.Decimal
import java.math.BigDecimal
import java.util.*
import com.orangefox.ripple.consolidation.contract.infrastructure.Contract as ContractEntity

abstract class ContractEvent(val contractId: UUID): DomainEvent(contractId) {
  fun contractId(): ContractId = ContractId(contractId)
}

data class ContractCreated(val contract: ContractEntity): ContractEvent(contract.id!!) {
  val contractorId = contract.contractor.uuid
}

data class TransactionPerformed(val contract: UUID, val amount: BigDecimal): ContractEvent(contract)
data class TransactionFailed(val contract: UUID, val amount: Decimal, val reason: String): ContractEvent(contract)
data class PaymentReceived(val contract: UUID, val contractor: UUID, val amount: BigDecimal): ContractEvent(contract)
data class ContractActivated(val id: UUID, val contractorId: UUID): ContractEvent(id)
data class ContractSuspended(val id: UUID, val contractorId: UUID, val reason: String): ContractEvent(id)
data class ContractBlocked(val id: UUID, val reason: String): ContractEvent(id)
data class ContractInvalidated(val id: UUID, val reason: String): ContractEvent(id)
data class ContractExpired(val id: UUID): ContractEvent(id)
data class ContractExtended(val id: UUID): ContractEvent(id)
data class ContractValueCapIncreased(val id: UUID, val delta: BigDecimal): ContractEvent(id)
data class ContractValueCapDecreased(val id: UUID, val delta: BigDecimal): ContractEvent(id)

