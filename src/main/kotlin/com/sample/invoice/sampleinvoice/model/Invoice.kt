package com.sample.invoice.sampleinvoice.model

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import java.time.LocalDate

@Entity
data class Invoice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val invoiceNumber: String,

    @Column
    val status: InvoiceStatus = InvoiceStatus.PENDING,

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    val issueDate: LocalDate,

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    val dueDate: LocalDate,

    @Embedded
    val client: Client,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "invoice_id")
    val items: List<InvoiceItem>
)

@Embeddable
data class Client(
    val name: String,
    val address: String,

    @Column(nullable = false)
    val contactEmail: String,

    val contactPhone: String
)