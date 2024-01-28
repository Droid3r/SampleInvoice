package com.sample.invoice.sampleinvoice.model.tariff

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("TIERED")
class TieredTariffPlan (
  name: String,
  description: String,
  val tier1Rate: Double = 0.10,
  val tier2Rate: Double = 0.2,
  val tier3Rate: Double = 0.35,
  val tier1Limit: Double = 100.0,
  val tier2Limit: Double = 200.0,
) : TariffPlan(name = name, description = description) {

  override fun getRatePerUnit(): Double {
    return tier1Rate
  }
}
