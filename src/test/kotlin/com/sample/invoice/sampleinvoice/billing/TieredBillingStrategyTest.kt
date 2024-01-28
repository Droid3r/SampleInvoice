package com.sample.invoice.sampleinvoice.billing


import com.sample.invoice.sampleinvoice.model.tariff.TieredTariffPlan
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TieredBillingStrategyTest {

  private val tieredTariffPlan = mockk<TieredTariffPlan>()

  @Test
  fun `calculateTotalAmount should return correct total amount for energy consumed within tier 1 limit`() {
    val strategy = TieredBillingStrategy()

    every { tieredTariffPlan.tier1Limit } returns 100.0
    every { tieredTariffPlan.tier1Rate } returns 0.1

    val energyConsumed = tieredTariffPlan.tier1Limit - 1.0
    val totalAmount = strategy.calculateTotalAmount(energyConsumed, tieredTariffPlan)

    assertEquals(energyConsumed * tieredTariffPlan.tier1Rate, totalAmount)
  }

  @Test
  fun `calculateTotalAmount should return correct total amount for energy consumed within tier 2 limit`() {
    val strategy = TieredBillingStrategy()

    every { tieredTariffPlan.tier1Limit } returns 100.0
    every { tieredTariffPlan.tier2Limit } returns 200.0
    every { tieredTariffPlan.tier1Rate } returns 0.1
    every { tieredTariffPlan.tier2Rate } returns 0.2

    val energyConsumed = tieredTariffPlan.tier2Limit - 1.0
    val totalAmount = strategy.calculateTotalAmount(energyConsumed, tieredTariffPlan)

    val expectedTotalAmount =
      (tieredTariffPlan.tier1Limit * tieredTariffPlan.tier1Rate) +
          ((energyConsumed - tieredTariffPlan.tier1Limit) * tieredTariffPlan.tier2Rate)
    assertEquals(expectedTotalAmount, totalAmount)
  }

  @Test
  fun `calculateTotalAmount should return correct total amount for energy consumed exceeding tier 2 limit`() {
    val strategy = TieredBillingStrategy()

    every { tieredTariffPlan.tier1Limit } returns 100.0
    every { tieredTariffPlan.tier2Limit } returns 200.0
    every { tieredTariffPlan.tier1Rate } returns 0.1
    every { tieredTariffPlan.tier2Rate } returns 0.2
    every { tieredTariffPlan.tier3Rate } returns 0.3

    val energyConsumed = tieredTariffPlan.tier2Limit + 1.0
    val totalAmount = strategy.calculateTotalAmount(energyConsumed, tieredTariffPlan)

    val expectedTotalAmount =
      (tieredTariffPlan.tier1Limit * tieredTariffPlan.tier1Rate) +
          ((tieredTariffPlan.tier2Limit - tieredTariffPlan.tier1Limit) * tieredTariffPlan.tier2Rate) +
          ((energyConsumed - tieredTariffPlan.tier2Limit) * tieredTariffPlan.tier3Rate)
    assertEquals(expectedTotalAmount, totalAmount)
  }
}