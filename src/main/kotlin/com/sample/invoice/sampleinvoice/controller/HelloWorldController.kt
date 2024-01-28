package com.sample.invoice.sampleinvoice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("api/hello")
@RestController
class HelloWorldController {

  @GetMapping
  fun helloWorld() = "Created this just to test the mapping of the controller"
}