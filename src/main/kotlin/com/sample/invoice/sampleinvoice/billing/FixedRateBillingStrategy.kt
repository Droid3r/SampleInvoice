package com.sample.invoice.sampleinvoice.billing

import com.sample.invoice.sampleinvoice.model.tariff.FixedRateTariffPlan
import com.sample.invoice.sampleinvoice.model.tariff.TariffPlan

class FixedRateBillingStrategy : BillingStrategy {

  override fun calculateTotalAmount(energyConsumed: Double, tariffPlan: TariffPlan): Double {
    return energyConsumed * (tariffPlan as FixedRateTariffPlan).getRatePerUnit()
  }
}

