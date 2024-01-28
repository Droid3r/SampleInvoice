package com.sample.invoice.sampleinvoice.service

import com.sample.invoice.sampleinvoice.model.Customer
import com.sample.invoice.sampleinvoice.model.tariff.FixedRateTariffPlan
import com.sample.invoice.sampleinvoice.repository.CustomerRepository
import com.sample.invoice.sampleinvoice.repository.TariffPlanRepository
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

class CustomerServiceTest {
  private val customerRepository: CustomerRepository = mockk()
  private val tariffPlanRepository: TariffPlanRepository = mockk()

  private lateinit var customerService: CustomerService

  @BeforeEach
  fun setUp() {
    customerService = CustomerService(customerRepository, tariffPlanRepository)
  }

  @Nested
  @DisplayName("Get All Customers")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetAllCustomers {

    @Test
    fun `getAllCustomers should return a list of customers`() {
      val sampleCustomers: List<Customer> = listOf(
        Customer(
          1L,
          "New Customer",
          "street",
          "new@example.com",
          tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
        ),
        Customer(
          2L,
          "New Customer 2",
          "street 2",
          "new2@example.com",
          tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
        )
      )

      every { customerRepository.findAll() } returns sampleCustomers

      val result = customerService.getAllCustomers()

      assert(result == sampleCustomers)
      verify(exactly = 1) { customerRepository.findAll() }
    }
  }

  @Nested
  @DisplayName("Get Customer by Id")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class GetCustomerById {

    @Test
    fun `getCustomerById should return the customer with the given ID`() {
      val customerId = 1L
      val sampleCustomer = Customer(
        customerId,
        "New Customer",
        "street",
        "new@example.com",
        tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
      )

      every { customerRepository.findById(customerId) } returns Optional.of(sampleCustomer)

      val result = customerService.getCustomerById(customerId)

      assert(result.name == "New Customer")
    }

    @Test
    fun `getCustomerById should throw NoSuchElementException when customer is not found`() {
      val customerId = 1L

      every { customerRepository.findById(customerId) } returns Optional.empty()

      assertThrows<NoSuchElementException> {
        customerService.getCustomerById(customerId)
      }

      verify(exactly = 1) { customerRepository.findById(customerId) }
    }
  }

  @Nested
  @DisplayName("Create a Customer")
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  inner class CreateCustomer {

    @Test
    fun `createCustomer should return the created customer`() {
      val customerToCreate = Customer(
        1L,
        "New Customer",
        "street",
        "new@example.com",
        tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
      )
      val savedTariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)

      every { tariffPlanRepository.save(any()) } returns savedTariffPlan
      every { customerRepository.save(any()) } returns customerToCreate.copy(id = 1, tariffPlan = savedTariffPlan)

      val result = customerService.createCustomer(customerToCreate)

      assert(result == customerToCreate.copy(id = 1, tariffPlan = savedTariffPlan))
      verify(exactly = 1) { tariffPlanRepository.save(any()) }
      verify(exactly = 1) { customerRepository.save(any()) }
    }
  }

  @Nested
  @DisplayName("Update a Customer")
  inner class UpdateCustomer {

    @Test
    fun `updateCustomer should return the updated customer`() {
      val customerId = 1L
      val existingCustomer = Customer(
        customerId,
        "Existing Customer",
        "Existing Address",
        "new@example.com",
        tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
      )
      val updatedCustomer = Customer(
        customerId,
        "Updated Customer",
        "Updated Address",
        "new@example.com",
        tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
      )
      val savedTariffPlan = FixedRateTariffPlan("fixed", "plan B", 1.0)

      every { customerRepository.findById(customerId) } returns Optional.of(existingCustomer)
      every { tariffPlanRepository.save(any()) } returns savedTariffPlan
      every { customerRepository.save(any()) } returns existingCustomer.copy(
        name = updatedCustomer.name,
        address = updatedCustomer.address,
        tariffPlan = savedTariffPlan
      )

      val result = customerService.updateCustomer(customerId, updatedCustomer)

      assert(
        result == existingCustomer.copy(
          name = updatedCustomer.name,
          address = updatedCustomer.address,
          tariffPlan = savedTariffPlan
        )
      )
      verify(exactly = 1) { customerRepository.findById(customerId) }
      verify(exactly = 1) { tariffPlanRepository.save(any()) }
      verify(exactly = 1) { customerRepository.save(any()) }
    }

    @Test
    fun `updateCustomer should throw NoSuchElementException when customer is not found`() {
      val customerId = 1L

      every { customerRepository.findById(customerId) } returns Optional.empty()

      assertThrows<NoSuchElementException> {
        customerService.updateCustomer(customerId, Customer(
          customerId,
          "Updated Customer",
          "Updated Address",
          "new@example.com",
          tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
        ))
      }

      verify(exactly = 1) { customerRepository.findById(customerId) }
    }
  }

  @Nested
  @DisplayName("Delete a Customer")
  inner class DeleteCustomer {

    @Test
    fun `deleteCustomer should delete the customer with the given ID`() {
      val customerId = 1L

      every { customerRepository.findById(customerId) } returns Optional.of(Customer(
        customerId,
        "Some Customer",
        "Some Address",
        "new@example.com",
        tariffPlan = FixedRateTariffPlan("fixed", "plan 2", 1.0)
      ))

      every { customerRepository.deleteById(customerId) } returns Unit

      customerService.deleteCustomer(customerId)

      verify(exactly = 1) { customerRepository.findById(customerId) }
      verify(exactly = 1) { customerRepository.deleteById(customerId) }
    }

    @Test
    fun `deleteCustomer should throw NoSuchElementException when customer is not found`() {
      val customerId = 1L

      every { customerRepository.findById(customerId) } returns Optional.empty()

      assertThrows<NoSuchElementException> {
        customerService.deleteCustomer(customerId)
      }

      verify(exactly = 1) { customerRepository.findById(customerId) }
    }
  }

}