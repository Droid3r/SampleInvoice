package com.sample.invoice.sampleinvoice.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.sample.invoice.sampleinvoice.billing.BillingStrategy
import com.sample.invoice.sampleinvoice.model.tariff.TariffPlan
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.LocalDate

@Entity
data class Invoice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    val status: InvoiceStatus = InvoiceStatus.PENDING,

    val energyConsumed: Double,

    val totalAmount: Double,

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    val issueDate: LocalDate,

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    val dueDate: LocalDate,

    val customerName: String,

    @ManyToOne
    @JoinColumn(name = "tariff_plan_id")
    val tariffPlan: TariffPlan,

    @Transient
    val billingStrategy: BillingStrategy,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "invoice_id")
    val invoiceItems: List<InvoiceItem>
)