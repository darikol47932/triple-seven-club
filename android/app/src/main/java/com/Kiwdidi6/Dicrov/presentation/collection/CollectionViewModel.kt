package com.Kiwdidi6.Dicrov.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kiwdidi6.Dicrov.core.config.GameConfig
import com.Kiwdidi6.Dicrov.data.sample.SampleData
import com.Kiwdidi6.Dicrov.domain.model.ClubProfile
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol
import com.Kiwdidi6.Dicrov.domain.repository.ClubRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CollectionViewModel(
    private val club: ClubRepository
) : ViewModel() {

    val state: StateFlow<CollectionUiState> = club.profile
        .map { toUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = toUiState(club.profile.value)
        )

    private fun toUiState(profile: ClubProfile): CollectionUiState {
        val cards = SlotSymbol.entries.map { symbol ->
            val reward = SampleData.decorations.firstOrNull { it.symbol == symbol }
            SymbolCardState(
                symbol = symbol,
                shards = profile.shardsOf(symbol),
                required = GameConfig.SHARDS_PER_SYMBOL,
                collected = profile.isSymbolCollected(symbol),
                rewardNameRes = reward?.nameRes ?: 0
            )
        }
        return CollectionUiState(
            symbols = cards,
            collectedCount = cards.count { it.collected },
            anyShards = cards.any { it.shards > 0 }
        )
    }
}
