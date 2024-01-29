package com.sample.invoice.sampleinvoice.controller


import com.fasterxml.jackson.databind.ObjectMapper
import com.sample.invoice.sampleinvoice.model.Customer
import com.sample.invoice.sampleinvoice.model.tariff.FixedRateTariffPlan
import com.sample.invoice.sampleinvoice.service.CustomerService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest @Autowired constructor(
  var mockMvc: MockMvc,
  val objectMapper: ObjectMapper
) {

  private val customerService: CustomerService = mockk()

  @InjectMocks
  private var controllerUnderTest: CustomerController = CustomerController(customerService)

  @BeforeEach
  fun setup() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build()
  }

  @Test
  fun getAllCustomers() {
    val sampleCustomers: List<Customer> = listOf(
      Customer(
        1L,
        "John Doe",
        "some street",
        "john@example.com",
        tariffPlan = FixedRateTariffPlan("fixed", "plan", 2.0)
      ),
      Customer(
        2L,
        "Jane Doe",
        "nice street",
        "jane@example.com",
        tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
      ),

      )
    every { customerService.getAllCustomers() } returns (sampleCustomers)

    mockMvc.get("/api/customers")
      .andExpect {
        status { isOk() }
        content { json(objectMapper.writeValueAsString(sampleCustomers)) }
      }
  }

  @Test
  fun getCustomerById() {
    val customerId = 1L
    val sampleCustomer = Customer(
      customerId,
      "John Doe",
      "street34",
      "john@example.com",
      tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
    )
    every { customerService.getCustomerById(customerId) } returns (sampleCustomer)

    mockMvc.get("/api/customers/{customerId}", customerId)
      .andDo { println() }
      .andExpect {
        println(this)
        status { isOk() }
      }
  }

  @Test
  fun createCustomer() {
    val newCustomer = Customer(
      1L,
      "New Customer",
      "street",
      "new@example.com",
      tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
    )
    val savedCustomer = Customer(
      1L,
      "New Customer",
      "street",
      "new@example.com",
      tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
    )
    every { customerService.createCustomer(any()) } returns (savedCustomer)

    mockMvc.post("/api/customers") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(newCustomer)
    }.andDo {
      println(this.print())

    }
      .andExpect {
        status { isOk() }
        content {
          jsonPath("$.name") { value(savedCustomer.name) } }
      }
  }

  @Test
  fun updateCustomer() {
    val customerId = 1L
    val updatedCustomer = Customer(
      customerId,
      "Updated Customer",
      "street 23",
      "updated@example.com",
      tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
    )
    every {  customerService.updateCustomer(any(), any())} returns (updatedCustomer)

    mockMvc.put("/api/customers/{customerId}", customerId) {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(updatedCustomer)
    }
      .andExpect {
        status { isOk() }
        content { json(objectMapper.writeValueAsString(updatedCustomer)) }
      }
  }

  @Test
  fun deleteCustomer() {
    val customerId = 1L

    every {  customerService.deleteCustomer(1L)} returns Unit

    mockMvc.delete("/api/customers/{customerId}", customerId)
      .andExpect { status { isOk() } }
  }
}
