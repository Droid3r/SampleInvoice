package com.sample.invoice.sampleinvoice.service

import com.sample.invoice.sampleinvoice.model.Customer
import com.sample.invoice.sampleinvoice.repository.CustomerRepository
import com.sample.invoice.sampleinvoice.repository.TariffPlanRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
  private val customerRepository: CustomerRepository,
  private val tariffPlanRepository: TariffPlanRepository
) {

  fun getAllCustomers(): List<Customer> {
    return customerRepository.findAll()
  }

  fun getCustomerById(customerId: Long): Customer {
    return customerRepository.findById(customerId)
      .orElseThrow { NoSuchElementException("Customer not found with ID: $customerId") }
  }

  fun createCustomer(customer: Customer): Customer {
    val savedTariffPlan = tariffPlanRepository.save(customer.tariffPlan)
    val customerToSave = customer.copy(tariffPlan = savedTariffPlan)
    return customerRepository.save(customerToSave)
  }

  fun updateCustomer(customerId: Long, updatedCustomer: Customer): Customer {
    val existingCustomer = getCustomerById(customerId)
    existingCustomer.tariffPlan.id?.let {
      tariffPlanRepository.deleteById(it)
    }
    val savedTariffPlan = tariffPlanRepository.save(updatedCustomer.tariffPlan)
    val customer = existingCustomer.copy(
      name = updatedCustomer.name,
      address = updatedCustomer.address,
      tariffPlan = savedTariffPlan
    )
    return customerRepository.save(customer)
  }

  fun deleteCustomer(customerId: Long) {
    getCustomerById(customerId)
    customerRepository.deleteById(customerId)
  }
}
