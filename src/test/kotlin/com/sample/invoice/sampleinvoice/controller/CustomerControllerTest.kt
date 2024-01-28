package com.sample.invoice.sampleinvoice.controller


import com.fasterxml.jackson.databind.ObjectMapper
import com.sample.invoice.sampleinvoice.model.Customer
import com.sample.invoice.sampleinvoice.model.tariff.FixedRateTariffPlan
import com.sample.invoice.sampleinvoice.service.CustomerService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest @Autowired constructor(
  val mockMvc: MockMvc,
  val objectMapper: ObjectMapper
) {

  private val customerService: CustomerService = mockk(relaxed = true)

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

    customerService.createCustomer(sampleCustomer)

    mockMvc.get("/api/customers/{customerId}", customerId)
      .andDo { println() }
      .andExpect {
        println(this)
        status { isOk() }
        //content { json(objectMapper.writeValueAsString(sampleCustomer)) }
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
    every { customerService.createCustomer(newCustomer) } returns (savedCustomer)

    mockMvc.post("/api/customers") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(newCustomer)
    }.andDo { println("==========")
      println(this.print())
    }
      .andExpect {
        status { isOk() }
        content { jsonPath("$.name") { value(savedCustomer.name) } }
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
    given(customerService.updateCustomer(customerId, updatedCustomer)).willReturn(updatedCustomer)

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

    mockMvc.delete("/api/customers/{customerId}", customerId)
      .andExpect { status { isNoContent() } }

    verify(customerService).deleteCustomer(customerId)
  }
}
