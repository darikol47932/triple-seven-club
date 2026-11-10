package com.Kiwdidi6.Dicrov.domain.model

enum class PayLine(val rows: List<Int>) {
    MIDDLE(listOf(1, 1, 1)),
    DESCENDING(listOf(0, 1, 2)),
    ASCENDING(listOf(2, 1, 0))
}
