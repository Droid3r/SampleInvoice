package com.sample.invoice.sampleinvoice.service

import com.sample.invoice.sampleinvoice.billing.BillingStrategy
import com.sample.invoice.sampleinvoice.billing.FixedRateBillingStrategy
import com.sample.invoice.sampleinvoice.billing.TieredBillingStrategy
import com.sample.invoice.sampleinvoice.model.Customer
import com.sample.invoice.sampleinvoice.model.Invoice
import com.sample.invoice.sampleinvoice.model.InvoiceItem
import com.sample.invoice.sampleinvoice.model.tariff.FixedRateTariffPlan
import com.sample.invoice.sampleinvoice.model.tariff.TariffPlan
import com.sample.invoice.sampleinvoice.model.tariff.TieredTariffPlan
import com.sample.invoice.sampleinvoice.repository.CustomerRepository
import com.sample.invoice.sampleinvoice.repository.InvoiceRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class InvoiceGenerationService(
  private val invoiceRepository: InvoiceRepository,
  private val customerRepository: CustomerRepository
) {

  fun generateInvoiceForCustomer(customerId: Long) {
      val customer = getCustomerById(customerId)
      generateInvoice(customer)
  }

  private fun getCustomerById(customerId: Long): Customer = customerRepository.findById(customerId)
    .orElseThrow { NoSuchElementException("Customer with id $customerId not found") }


  private fun generateInvoice(customer: Customer) {
    val currentDate = LocalDate.now()
    val dueDate = currentDate.plusMonths(1)

    val energyConsumed = calculateEnergyConsumedForCustomer(customer.id)
    val billingStrategy = getBillingStrategyForTariffPlan(customer.tariffPlan)
    val totalAmount = billingStrategy.calculateTotalAmount(energyConsumed, customer.tariffPlan)

    val invoice = Invoice(
      customerName = customer.name,
      energyConsumed = energyConsumed,
      issueDate = currentDate,
      dueDate = dueDate,
      tariffPlan = customer.tariffPlan,
      billingStrategy = billingStrategy,
      totalAmount = totalAmount,
      invoiceItems = mutableListOf(
        InvoiceItem(
          description = "Energy Usage",
          quantity = energyConsumed,
          unitPrice = customer.tariffPlan.getRatePerUnit()
        )
      )
    )

    saveInvoice(invoice)
  }

  private fun calculateEnergyConsumedForCustomer(customerId: Long): Double {
    return 100.0 //TODO add consumption to DB linked to each customer
  }

  private fun getBillingStrategyForTariffPlan(tariffPlan: TariffPlan): BillingStrategy {
    return when (tariffPlan) {
      is FixedRateTariffPlan -> FixedRateBillingStrategy()
      is TieredTariffPlan -> TieredBillingStrategy()
      else -> throw IllegalArgumentException("Unsupported tariff plan type")
    }
  }

  private fun saveInvoice(invoice: Invoice) {
    invoiceRepository.save(invoice)
  }
}


