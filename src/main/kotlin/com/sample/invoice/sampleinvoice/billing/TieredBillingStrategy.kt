package com.sample.invoice.sampleinvoice.billing

import com.sample.invoice.sampleinvoice.model.tariff.TariffPlan
import com.sample.invoice.sampleinvoice.model.tariff.TieredTariffPlan


class TieredBillingStrategy : BillingStrategy {

  override fun calculateTotalAmount(energyConsumed: Double, tariffPlan: TariffPlan): Double {
    val tieredTariffPlan = tariffPlan as TieredTariffPlan
    return when {
      energyConsumed <= tieredTariffPlan.tier1Limit -> energyConsumed * tieredTariffPlan.tier1Rate
      energyConsumed <= tieredTariffPlan.tier2Limit -> (tieredTariffPlan.tier1Limit * tieredTariffPlan.tier1Rate) +
          ((energyConsumed - tieredTariffPlan.tier1Limit) * tieredTariffPlan.tier2Rate)
      else -> (tieredTariffPlan.tier1Limit * tieredTariffPlan.tier1Rate) +
          ((tieredTariffPlan.tier2Limit - tieredTariffPlan.tier1Limit) * tieredTariffPlan.tier2Rate) +
          ((energyConsumed - tieredTariffPlan.tier2Limit) * tieredTariffPlan.tier3Rate)
    }
  }
}
