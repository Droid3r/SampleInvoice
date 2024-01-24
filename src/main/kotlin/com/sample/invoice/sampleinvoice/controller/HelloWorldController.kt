package com.sample.invoice.sampleinvoice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("api/hello")
@RestController
class HelloWorldController {

  @GetMapping
  fun helloWorld() = "This is Spartaaaaaa! Get ready for the fight..."
}