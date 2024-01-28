package com.sample.invoice.sampleinvoice.billing

import com.sample.invoice.sampleinvoice.model.tariff.FixedRateTariffPlan
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FixedRateBillingStrategyTest {
  private val fixedRateTariffPlan = mockk<FixedRateTariffPlan>()

  @Test
  fun `calculateTotalAmount should return correct total amount`() {
    val strategy = FixedRateBillingStrategy()
    val energyConsumed = 150.0
    val ratePerUnit = 0.15
    every { fixedRateTariffPlan.getRatePerUnit() } returns ratePerUnit

    val totalAmount = strategy.calculateTotalAmount(energyConsumed, fixedRateTariffPlan)

    assertEquals(energyConsumed * ratePerUnit, totalAmount)
  }
}