package com.sample.invoice.sampleinvoice.service

import com.sample.invoice.sampleinvoice.model.Invoice
import com.sample.invoice.sampleinvoice.model.InvoiceStatus
import com.sample.invoice.sampleinvoice.repository.InvoiceRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class InvoiceService(
  private val invoiceRepository: InvoiceRepository,
  private val emailService: EmailService // TODO : enhancement, create a notification service
) {
  fun getAllInvoices(): List<Invoice> = invoiceRepository.findAll()

  fun getInvoiceById(id: Long): Invoice? = invoiceRepository.findById(id).orElse(null)

  fun createInvoice(invoice: Invoice): Invoice {
    val createdInvoice = invoiceRepository.save(invoice)
    emailService.sendInvoiceNotification(createdInvoice)
    return createdInvoice
  }

  fun updateInvoice(id: Long, updatedInvoice: Invoice): Invoice {
    require(id == updatedInvoice.id) { "Invoice ID in the path and the body must be same" }
    val updated = invoiceRepository.save(updatedInvoice)
    emailService.sendInvoiceUpdateNotification(updated)
    return updated
  }

  fun deleteInvoice(id: Long) = invoiceRepository.deleteById(id)

  fun getOverdueInvoices(): List<Invoice> {
    val currentDate = LocalDate.now()
    return invoiceRepository.findAllByDueDateBeforeAndStatus(currentDate, InvoiceStatus.PENDING)
  }
}
