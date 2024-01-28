package com.sample.invoice.sampleinvoice.service

import com.sample.invoice.sampleinvoice.model.tariff.TariffPlan
import com.sample.invoice.sampleinvoice.repository.TariffPlanRepository
import org.springframework.stereotype.Service

@Service
class TariffPlanService(private val tariffPlanRepository: TariffPlanRepository) {
  fun getAllTariffPlans(): List<TariffPlan> = tariffPlanRepository.findAll()

  fun getTariffPlanById(id: Long): TariffPlan? = tariffPlanRepository.findById(id).orElse(null)

  fun createTariffPlan(tariffPlan: TariffPlan): TariffPlan = tariffPlanRepository.save(tariffPlan)

  fun updateTariffPlan(id: Long, updatedTariffPlan: TariffPlan): TariffPlan {
    require(id == updatedTariffPlan.id) { "TariffPlan ID must match in the path and body." }
    return tariffPlanRepository.save(updatedTariffPlan)
  }

  suspend fun deleteTariffPlan(id: Long) = tariffPlanRepository.deleteById(id)


}
