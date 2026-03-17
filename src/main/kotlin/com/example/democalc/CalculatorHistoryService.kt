package com.example.democalc

import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong

@Service
class CalculatorHistoryService {

    companion object {
        private const val MAX_SIZE = 100
        private val SUPPORTED_OPERATORS = setOf("+", "-", "*", "/")
    }

    private val history = ArrayDeque<HistoryItem>()
    private val idCounter = AtomicLong(0)

    fun save(a: Double, b: Double, operator: String, result: Double) {
        val item = HistoryItem(
            id = idCounter.incrementAndGet(),
            a = a,
            b = b,
            operator = operator,
            result = result,
            createdAt = LocalDateTime.now()
        )
        history.addFirst(item)
        if (history.size > MAX_SIZE) {
            history.removeLast()
        }
    }

    fun getHistory(operator: String? = null): List<HistoryItem> {
        if (operator != null && operator !in SUPPORTED_OPERATORS) {
            throw IllegalArgumentException("지원하지 않는 연산자입니다: $operator")
        }
        return if (operator == null) history.toList() else history.filter { it.operator == operator }
    }

    fun clearAll() {
        history.clear()
    }
}
