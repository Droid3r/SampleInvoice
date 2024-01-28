package com.sample.invoice.sampleinvoice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.sample.invoice.sampleinvoice"])
class SampleInvoiceApplication

fun main(args: Array<String>) {
	runApplication<SampleInvoiceApplication>(*args)
}