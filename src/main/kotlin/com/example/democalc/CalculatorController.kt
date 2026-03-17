package com.example.democalc

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class CalculatorController(
    private val calculatorService: CalculatorService,
    private val historyService: CalculatorHistoryService
) {

    @PostMapping("/calculate")
    fun calculate(@RequestBody request: CalculatorRequest): ResponseEntity<CalculatorResponse> {
        val response = calculatorService.calculate(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/history")
    fun getHistory(): ResponseEntity<List<HistoryItem>> {
        return ResponseEntity.ok(historyService.getAll())
    }
}
