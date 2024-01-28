package com.sample.invoice.sampleinvoice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.sample.invoice.sampleinvoice.billing.FixedRateBillingStrategy
import com.sample.invoice.sampleinvoice.model.Invoice
import com.sample.invoice.sampleinvoice.model.InvoiceItem
import com.sample.invoice.sampleinvoice.model.tariff.FixedRateTariffPlan
import com.sample.invoice.sampleinvoice.service.InvoiceGenerationService
import com.sample.invoice.sampleinvoice.service.InvoiceService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.InjectMocks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDate


@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerTest @Autowired constructor(
  var mockMvc: MockMvc,
  val objectMapper: ObjectMapper
) {

  private val invoiceService: InvoiceService = mockk()
  private val invoiceGenerationService: InvoiceGenerationService = mockk()

  @InjectMocks
  private var controllerUnderTest: InvoiceController = InvoiceController(invoiceService, invoiceGenerationService)

  private val baseUrl = "/api/invoices"

  @BeforeEach
  fun setup() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build()
    configureObjectMapper(objectMapper)
  }

  private fun configureObjectMapper(objectMapper: ObjectMapper) {
    val module = SimpleModule()
    module.addSerializer(ToStringSerializer(FixedRateBillingStrategy::class.java))
    objectMapper.registerModule(module)
  }

  @Nested
  @DisplayName("GET /api/invoices")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetInvoices {

    @Test
    fun `getAllInvoices should return 200`() {
      every { invoiceService.getAllInvoices() } returns (emptyList())
      mockMvc.get(baseUrl)
        .andExpect { status { isOk() } }
    }

    @Test
    fun `getAllInvoices should return invoices`() {
      val sampleInvoices: List<Invoice> = listOf(
        Invoice(
          customerName = "Test Person",
          energyConsumed = 200.0,
          issueDate = LocalDate.now(),
          dueDate = LocalDate.now().plusMonths(1),
          tariffPlan = FixedRateTariffPlan("Fixed Rate Plan", "Some Description", 0.10),
          billingStrategy = FixedRateBillingStrategy(),
          totalAmount = 300.0,
          invoiceItems = mutableListOf(
            InvoiceItem(2, "Energy Usage", 100.0, 0.10)
          )
        ),
        Invoice(
          customerName = "Test Person 2",
          energyConsumed = 220.0,
          issueDate = LocalDate.now(),
          dueDate = LocalDate.now().plusMonths(2),
          tariffPlan = FixedRateTariffPlan("Fixed Rate Plan", "Some Description 2", 0.20),
          billingStrategy = FixedRateBillingStrategy(),
          totalAmount = 330.0,
          invoiceItems = mutableListOf(
            InvoiceItem(3, "Energy Usage", 200.0, 0.10)
          )
        )
      )
      every { invoiceService.getAllInvoices() } returns (sampleInvoices)

      val performGet = mockMvc.get(baseUrl)

      performGet.andExpect {
        status { isOk() }
        content {
          contentType(MediaType.APPLICATION_JSON)
          jsonPath("$[0].customerName") { value("Test Person") }
        }
      }
    }
  }

  @Nested
  @DisplayName("GET /api/invoices/{id}")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetInvoiceById {
    @Test
    fun `getInvoiceById should return 200`() {
      val invoiceId = 1L
      val sampleInvoice = Invoice(
        id = invoiceId,
        customerName = "Test Person",
        energyConsumed = 200.0,
        issueDate = LocalDate.now(),
        dueDate = LocalDate.now().plusMonths(1),
        tariffPlan = FixedRateTariffPlan("Fixed Rate Plan", "Some Description", 0.10),
        billingStrategy = FixedRateBillingStrategy(),
        totalAmount = 300.0,
        invoiceItems = mutableListOf(
          InvoiceItem(2, "Energy Usage", 100.0, 0.10)
        )
      )
      every { invoiceService.getInvoiceById(invoiceId) } returns sampleInvoice

      mockMvc.get("$baseUrl/{id}", invoiceId)
        .andExpect {
          status { isOk() }
          content { contentType(MediaType.APPLICATION_JSON) }
          jsonPath("$.id") { value(invoiceId.toInt()) }
          jsonPath("$.customerName") { value("Test Person") }
        }

      verify(exactly = 1) { invoiceService.getInvoiceById(invoiceId) }
    }

    @Test
    fun `getInvoiceById should return 404`() {
      val invoiceId = 2L
      every { invoiceService.getInvoiceById(invoiceId) } returns null

      mockMvc.get("$baseUrl/{id}", invoiceId)
        .andExpect { status { isNotFound() } }

      verify(exactly = 1) { invoiceService.getInvoiceById(invoiceId) }
    }
  }

  @Nested
  @DisplayName("GET /api/invoices/generate/{customerId}")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetInvoiceForCustomer {
    @Test
    fun `generateInvoicesForAllCustomers should return success message`() {
      val customerId = 1L
      every { invoiceGenerationService.generateInvoiceForCustomer(customerId) } returns Unit

      mockMvc.get("$baseUrl/generate/{customerId}", customerId)
        .andExpect {
          status { isOk() }
          content { string("Invoices generated successfully.") }
        }

      verify(exactly = 1) { invoiceGenerationService.generateInvoiceForCustomer(customerId) }
    }
  }

  @Nested
  @DisplayName("POST /api/invoices")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class CreateNewInvoice {
    @Test
    fun `createInvoice should return 201`() {
      val invoiceToCreate = Invoice(
        customerName = "Test Person",
        energyConsumed = 200.0,
        issueDate = LocalDate.now(),
        dueDate = LocalDate.now().plusMonths(1),
        tariffPlan = FixedRateTariffPlan("Fixed Rate Plan", "Some Description", 0.10),
        billingStrategy = FixedRateBillingStrategy(),
        totalAmount = 300.0,
        invoiceItems = mutableListOf(
          InvoiceItem(2, "Energy Usage", 100.0, 0.10)
        )
      )
      val createdInvoice = Invoice(
        id = 1L,
        customerName = "Test Person",
        energyConsumed = 200.0,
        issueDate = LocalDate.now(),
        dueDate = LocalDate.now().plusMonths(1),
        tariffPlan = FixedRateTariffPlan("Fixed Rate Plan", "Some Description", 0.10),
        billingStrategy = FixedRateBillingStrategy(),
        totalAmount = 300.0,
        invoiceItems = mutableListOf(
          InvoiceItem(2, "Energy Usage", 100.0, 0.10)
        )
      )

      every { invoiceService.createInvoice(invoiceToCreate) } returns createdInvoice

      mockMvc.post(baseUrl) {
        contentType = MediaType.APPLICATION_JSON
        content = objectMapper.writeValueAsString(invoiceToCreate)
      }
        .andExpect {
          status { isCreated() }
        }

      verify(exactly = 1) { invoiceService.createInvoice(invoiceToCreate) }
    }
  }

  @Nested
  @DisplayName("GET /api/invoices/overdue")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetOverdueInvoices {

    @Test
    fun `getOverdueInvoices should return 200 with a list of overdue invoices`() {
      val sampleOverdueInvoices: List<Invoice> = listOf(
        Invoice(
          customerName = "Test Person 1",
          energyConsumed = 200.0,
          issueDate = LocalDate.now(),
          dueDate = LocalDate.now().minusMonths(1),
          tariffPlan = FixedRateTariffPlan("Fixed Rate Plan", "Some Description", 0.10),
          billingStrategy = FixedRateBillingStrategy(),
          totalAmount = 300.0,
          invoiceItems = mutableListOf(
            InvoiceItem(2, "Energy Usage", 100.0, 0.10)
          )
        ),
        Invoice(
          customerName = "Test Person 2",
          energyConsumed = 100.0,
          issueDate = LocalDate.now(),
          dueDate = LocalDate.now().minusMonths(1),
          tariffPlan = FixedRateTariffPlan("Fixed Rate Plan", "Some Description", 0.10),
          billingStrategy = FixedRateBillingStrategy(),
          totalAmount = 230.0,
          invoiceItems = mutableListOf(
            InvoiceItem(3, "Energy Usage", 110.0, 0.10)
          )
        )
      )

      every { invoiceService.getOverdueInvoices() } returns sampleOverdueInvoices

      mockMvc.get("$baseUrl/overdue")
        .andDo { println(this) }
        .andExpect {
          status { isOk() }
          content {
            contentType(MediaType.APPLICATION_JSON)
            jsonPath("$[0].customerName") { value("Test Person 1") }
            jsonPath("$[1].customerName") { value("Test Person 2") }
          }
        }

      verify(exactly = 1) { invoiceService.getOverdueInvoices() }
    }
  }

}

