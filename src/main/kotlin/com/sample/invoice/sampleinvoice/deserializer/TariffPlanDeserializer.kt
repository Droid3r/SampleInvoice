package com.sample.invoice.sampleinvoice.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.sample.invoice.sampleinvoice.model.tariff.FixedRateTariffPlan
import com.sample.invoice.sampleinvoice.model.tariff.TariffPlan
import com.sample.invoice.sampleinvoice.model.tariff.TieredTariffPlan

class TariffPlanDeserializer : StdDeserializer<TariffPlan>(TariffPlan::class.java) {

  override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): TariffPlan {
    val node: JsonNode = parser.codec.readTree(parser)
    val type = node.get("type").asText()

    return when (type) {
      "FIXED_RATE" -> parser.codec.treeToValue(node, FixedRateTariffPlan::class.java)
      "TIERED" -> parser.codec.treeToValue(node, TieredTariffPlan::class.java)
      else -> throw IllegalArgumentException("Unsupported tariff plan type: $type")
    }
  }
}

