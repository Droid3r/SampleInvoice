package com.sample.invoice.sampleinvoice.repository

import com.sample.invoice.sampleinvoice.model.tariff.TariffPlan
import org.springframework.data.jpa.repository.JpaRepository

interface TariffPlanRepository : JpaRepository<TariffPlan, Long>
