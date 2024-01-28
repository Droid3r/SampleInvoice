package com.sample.invoice.sampleinvoice.repository

import com.sample.invoice.sampleinvoice.model.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Long>