package com.sample.invoice.sampleinvoice.controller

import com.sample.invoice.sampleinvoice.model.Customer
import com.sample.invoice.sampleinvoice.service.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
class CustomerController(private val customerService: CustomerService) {

  @GetMapping
  fun getAllCustomers(): List<Customer> {
    return customerService.getAllCustomers()
  }

  @GetMapping("/{customerId}")
  fun getCustomerById(@PathVariable customerId: Long): Customer {
    return customerService.getCustomerById(customerId)
  }

  @PostMapping
  fun createCustomer(@RequestBody customer: Customer): Customer {
    return customerService.createCustomer(customer)
  }

  @PutMapping("/{customerId}")
  fun updateCustomer(@PathVariable customerId: Long, @RequestBody updatedCustomer: Customer): Customer {
    return customerService.updateCustomer(customerId, updatedCustomer)
  }

  @DeleteMapping("/{customerId}")
  fun deleteCustomer(@PathVariable customerId: Long) {
    customerService.deleteCustomer(customerId)
  }

  @PostMapping("/{customerId}/update-usage")
  fun updateCustomerUsage(
    @PathVariable customerId: Long,
    @RequestParam newUsage: Double
  ): ResponseEntity<Customer> {
    val updatedCustomer = customerService.updateCustomerEnergyUsage(customerId, newUsage)
    return ResponseEntity.ok(updatedCustomer)
  }
}