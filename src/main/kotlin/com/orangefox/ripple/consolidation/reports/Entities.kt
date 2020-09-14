package com.orangefox.ripple.consolidation.reports

import org.springframework.data.annotation.Id
import java.math.BigDecimal
import java.time.LocalDate


data class SaleLog(
  val issueDate: LocalDate,
  val paxName: String,
  val pnr: String,
  val ticketNumber: String,
  val docType: String,
  val airline: String,
  val agency: String,
  val user: String,
  val fare: BigDecimal,
  val equivalentFare: BigDecimal,
  val taxes: BigDecimal,
  val otherCharges: BigDecimal,
  val discountReceivable: BigDecimal,
  val discountPayable: BigDecimal,
  val commissionReceivable: BigDecimal,
  val commissionPayable: BigDecimal,
  val wht: BigDecimal,
  val totalReceivable: BigDecimal,
  val totalPayable: BigDecimal,
  val status: String
) {
  @Id var id: Long? = null
}

data class ExchangeLog(
  val issueDate: LocalDate,
  val paxName: String,
  val pnr: String,
  val ticketNumber: String,
  val docType: String,
  val airline: String,
  val agency: String,
  val user: String,
  val fare: BigDecimal,
  val equivalentFare: BigDecimal,
  val taxes: BigDecimal,
  val otherCharges: BigDecimal,
  val discountReceivable: BigDecimal,
  val discountPayable: BigDecimal,
  val commissionReceivable: BigDecimal,
  val commissionPayable: BigDecimal,
  val wht: BigDecimal,
  val totalReceivable: BigDecimal,
  val totalPayable: BigDecimal,
  val status: String,
  val exchangeFor: String,
  val exchangeDate: LocalDate,
  val fareDifference: BigDecimal,
  val airlineCharges: BigDecimal,
  val consolidatorCharges: BigDecimal,
  val consolidatorPayable: BigDecimal,
  val consolidatorReceivable: BigDecimal
) {
  @Id val id: Long? = null
}

data class RefundLog(
  val issueDate: LocalDate,
  val paxName: String,
  val pnr: String,
  val ticketNumber: String,
  val docType: String,
  val airline: String,
  val agency: String,
  val user: String,
  val fare: BigDecimal,
  val equivalentFare: BigDecimal,
  val taxes: BigDecimal,
  val otherCharges: BigDecimal,
  val discountReceivable: BigDecimal,
  val discountPayable: BigDecimal,
  val commissionReceivable: BigDecimal,
  val commissionPayable: BigDecimal,
  val wht: BigDecimal,
  val totalReceivable: BigDecimal,
  val totalPayable: BigDecimal,
  val status: String,

  val refundDate: LocalDate,
  val fareUsed: BigDecimal,
  val fareRefundable: BigDecimal,
  val taxRefundable: BigDecimal
) {
  @Id val id: Long? = null
}

data class VoidLog(
  val issueDate: LocalDate,
  val paxName: String,
  val pnr: String,
  val ticketNumber: String,
  val docType: String,
  val airline: String,
  val agency: String,
  val user: String,
  val fare: BigDecimal,
  val voidChargesReceivable: BigDecimal,
  val voidChargesPayable: BigDecimal,
  val totalPayable: BigDecimal,
  val totalReceivable: BigDecimal
) {
  @Id val id: Long? = null
}
