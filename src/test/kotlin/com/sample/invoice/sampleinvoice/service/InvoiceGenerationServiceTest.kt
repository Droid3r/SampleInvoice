package com.sample.invoice.sampleinvoice.service

import com.sample.invoice.sampleinvoice.model.Customer
import com.sample.invoice.sampleinvoice.model.Invoice
import com.sample.invoice.sampleinvoice.model.tariff.FixedRateTariffPlan
import com.sample.invoice.sampleinvoice.repository.CustomerRepository
import com.sample.invoice.sampleinvoice.repository.InvoiceRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.util.Optional

class InvoiceGenerationServiceTest {

  private val invoiceRepository: InvoiceRepository = mockk()
  private val customerRepository: CustomerRepository = mockk()

  private lateinit var invoiceGenerationService: InvoiceGenerationService

  @BeforeEach
  fun setUp() {
    invoiceGenerationService = InvoiceGenerationService(invoiceRepository, customerRepository)
  }

  @Nested
  @DisplayName("When generating invoice for customer")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GenerateInvoiceForCustomer {

    @Test
    fun `generateInvoiceForCustomer should generate invoice for existing customer`() {
      val customerId = 1L
      val customer = Customer(
        customerId,
        "New Customer",
        "street",
        "new@example.com",
        tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
      )

      every { customerRepository.findById(customerId) } returns Optional.of(customer)
      every { invoiceRepository.save(any()) } returns mockk<Invoice>()

      invoiceGenerationService.generateInvoiceForCustomer(customerId)

      verify(exactly = 1) { customerRepository.findById(customerId) }
      verify(exactly = 1) { invoiceRepository.save(any()) }

    }

    @Test
    fun `generateInvoiceForCustomer should throw NoSuchElementException for non-existing customer`() {
      val customerId = 1L

      every { customerRepository.findById(customerId) } returns Optional.empty()

      assertThrows<NoSuchElementException> {
        invoiceGenerationService.generateInvoiceForCustomer(customerId)
      }
    }
  }
}
