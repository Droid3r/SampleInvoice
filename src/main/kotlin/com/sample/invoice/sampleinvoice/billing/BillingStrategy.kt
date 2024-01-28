package com.sample.invoice.sampleinvoice.billing

import com.sample.invoice.sampleinvoice.model.tariff.TariffPlan

interface BillingStrategy {
  fun calculateTotalAmount(energyConsumed: Double, tariffPlan: TariffPlan): Double
}
