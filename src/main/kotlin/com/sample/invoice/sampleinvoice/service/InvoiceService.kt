package com.sample.invoice.sampleinvoice.service

import com.sample.invoice.sampleinvoice.model.Invoice
import com.sample.invoice.sampleinvoice.model.InvoiceStatus
import com.sample.invoice.sampleinvoice.repository.InvoiceRepository
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class InvoiceService(
    private val invoiceRepository: InvoiceRepository,
    private val emailService: EmailService // Assume an EmailService for notification
) {
  suspend fun getAllInvoices(): List<Invoice> = coroutineScope {
    invoiceRepository.findAll()
  }

  suspend fun getInvoiceById(id: Long): Invoice? = coroutineScope {
    invoiceRepository.findById(id).orElse(null)
  }

  suspend fun createInvoice(invoice: Invoice): Invoice = coroutineScope {
    val createdInvoice = invoiceRepository.save(invoice)
    emailService.sendInvoiceNotification(createdInvoice)
    createdInvoice
  }

  suspend fun updateInvoice(id: Long, updatedInvoice: Invoice): Invoice = coroutineScope {
    require(id == updatedInvoice.id) { "Invoice ID in the path and the body must be same" }
    val updated = invoiceRepository.save(updatedInvoice)
    emailService.sendInvoiceUpdateNotification(updated)
    updated
  }

  suspend fun deleteInvoice(id: Long) {
    coroutineScope {
      invoiceRepository.deleteById(id)
    }
  }

  suspend fun getOverdueInvoices(): List<Invoice> = coroutineScope {
    val currentDate = LocalDate.now()
    invoiceRepository.findAllByDueDateBeforeAndStatus(currentDate, InvoiceStatus.PENDING)
  }
}
