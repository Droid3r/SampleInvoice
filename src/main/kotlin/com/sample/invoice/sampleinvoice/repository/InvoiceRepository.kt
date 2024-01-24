package com.sample.invoice.sampleinvoice.repository

import com.sample.invoice.sampleinvoice.model.Invoice
import com.sample.invoice.sampleinvoice.model.InvoiceStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface InvoiceRepository : JpaRepository<Invoice, Long> {
  fun findAllByStatus(status: InvoiceStatus): List<Invoice>
  fun findAllByDueDateBeforeAndStatus(dueDate: LocalDate, status: InvoiceStatus): List<Invoice>
}