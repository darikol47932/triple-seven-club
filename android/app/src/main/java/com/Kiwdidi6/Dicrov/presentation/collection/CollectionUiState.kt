package com.Kiwdidi6.Dicrov.presentation.collection

import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol

data class SymbolCardState(
    val symbol: SlotSymbol,
    val shards: Int,
    val required: Int,
    val collected: Boolean,
    val rewardNameRes: Int
)

data class CollectionUiState(
    val symbols: List<SymbolCardState> = emptyList(),
    val collectedCount: Int = 0,
    val totalSymbols: Int = SlotSymbol.entries.size,
    val anyShards: Boolean = false
)
