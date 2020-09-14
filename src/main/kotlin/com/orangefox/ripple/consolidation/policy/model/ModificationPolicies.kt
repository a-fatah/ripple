package com.orangefox.ripple.consolidation.policy.model

import com.orangefox.ripple.reservations.farequote.model.Decimal
import com.orangefox.ripple.reservations.farequote.model.Fare

abstract class FareModifier(val adjustment: FareAdjustment) {
  abstract fun modifyFare(fare: Fare): Fare
}

class Discount(adjustment: FareAdjustment) : FareModifier(adjustment) {
  override fun modifyFare(fare: Fare): Fare {
    val current = fare.adjustment
    val newAdjustment = adjustment.calculate(fare).negate()
    return fare.copy(adjustment = current.plus(newAdjustment))
  }
}

class Commission(adjustment: FareAdjustment) : FareModifier(adjustment) {
  override fun modifyFare(fare: Fare): Fare {
    val current = fare.adjustment
    val newAdjustment = adjustment.calculate(fare)
    return fare.copy(adjustment = current.plus(newAdjustment))
  }
}

abstract class FareAdjustment(val name: String) {
  abstract fun calculate(fare: Fare): Decimal
}

class FlatAdjustment(displayName: String, val param: Decimal): FareAdjustment(displayName) {
  override fun calculate(fare: Fare): Decimal {
    return param
  }
}

class PercentageOfBaseFare(displayName: String, val param: Decimal): FareAdjustment(displayName) {
  override fun calculate(fare: Fare): Decimal {
    return fare.baseAmount * param / Decimal(100)
  }
}

class PercentageOfTotal(displayName: String, val param: Decimal): FareAdjustment(displayName) {
  override fun calculate(fare: Fare): Decimal {
    return fare.totalAmount * param / Decimal(100)
  }
}

class PercentageOfTaxes(displayName: String, val param: Decimal): FareAdjustment(displayName) {
  override fun calculate(fare: Fare): Decimal {
    return fare.taxes * param / Decimal(100)
  }
}
