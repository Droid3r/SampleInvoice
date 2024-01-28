package com.sample.invoice.sampleinvoice.model.tariff

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("FIXED_RATE")
class FixedRateTariffPlan (
  name: String,
  description: String,
  val rate: Double = 0.10
) : TariffPlan(name = name, description = description) {

  override fun getRatePerUnit(): Double = rate
}
