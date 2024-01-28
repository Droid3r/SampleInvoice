package com.sample.invoice.sampleinvoice.model

import com.sample.invoice.sampleinvoice.model.tariff.TariffPlan
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
data class Customer(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long,
  val name: String,
  val address: String,

  @Column(nullable = false)
  val email: String,

  @ManyToOne(cascade = [CascadeType.PERSIST])
  @JoinColumn(name = "tariff_plan_id")
  val tariffPlan: TariffPlan
)