package com.Kiwdidi6.Dicrov.core.ui

import java.util.Locale

fun formatAmount(value: Int): String = String.format(Locale.US, "%,d", value)

fun formatMultiplier(value: Float): String {
    val raw = String.format(Locale.US, "%.2f", value)
    val trimmed = raw.trimEnd('0').trimEnd('.')
    return "x$trimmed"
}

fun formatRatio(current: Int, total: Int): String = "$current / $total"
