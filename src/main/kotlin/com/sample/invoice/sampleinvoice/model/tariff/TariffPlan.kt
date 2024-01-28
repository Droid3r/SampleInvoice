package com.sample.invoice.sampleinvoice.model.tariff

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.sample.invoice.sampleinvoice.deserializer.TariffPlanDeserializer
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.DiscriminatorType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tariff_plan_type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(value = FixedRateTariffPlan::class, name = "FIXED_RATE"),
  JsonSubTypes.Type(value = TieredTariffPlan::class, name = "TIERED")
)
abstract class TariffPlan(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  open val id: Long? = null,
  open val name: String,
  open val description: String
) {
  abstract fun getRatePerUnit(): Double
}
