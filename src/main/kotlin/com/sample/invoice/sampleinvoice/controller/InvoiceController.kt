package com.sample.invoice.sampleinvoice.controller

import com.sample.invoice.sampleinvoice.model.Invoice
import com.sample.invoice.sampleinvoice.service.InvoiceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/invoices")
class InvoiceController(private val invoiceService: InvoiceService) {

  @GetMapping
  suspend fun getAllInvoices(): List<Invoice> = invoiceService.getAllInvoices()

  @GetMapping("/{id}")
  suspend fun getInvoiceById(@PathVariable id: Long): ResponseEntity<Invoice> {
    val invoice = invoiceService.getInvoiceById(id)
    return if (invoice != null) ResponseEntity.ok(invoice) else ResponseEntity.notFound().build()
  }

  @PostMapping
  suspend fun createInvoice(@RequestBody invoice: Invoice): ResponseEntity<Invoice> {
    val createdInvoice = invoiceService.createInvoice(invoice)
    return ResponseEntity.created(URI.create("/api/invoices/${createdInvoice.id}")).body(createdInvoice)
  }

  @PutMapping("/{id}")
  suspend fun updateInvoice(@PathVariable id: Long, @RequestBody updatedInvoice: Invoice): ResponseEntity<Invoice> {
    val updated = invoiceService.updateInvoice(id, updatedInvoice)
    return ResponseEntity.ok(updated)
  }

  @DeleteMapping("/{id}")
  suspend fun deleteInvoice(@PathVariable id: Long): ResponseEntity<Void> {
    invoiceService.deleteInvoice(id)
    return ResponseEntity.noContent().build()
  }

  @GetMapping("/overdue")
  suspend fun getOverdueInvoices(): List<Invoice> = invoiceService.getOverdueInvoices()
}
