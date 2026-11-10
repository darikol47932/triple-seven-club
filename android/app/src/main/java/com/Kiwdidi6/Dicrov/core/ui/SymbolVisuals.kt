package com.Kiwdidi6.Dicrov.core.ui

import androidx.compose.ui.graphics.Color
import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.core.theme.Danger
import com.Kiwdidi6.Dicrov.core.theme.Gold
import com.Kiwdidi6.Dicrov.core.theme.GoldLight
import com.Kiwdidi6.Dicrov.core.theme.Success
import com.Kiwdidi6.Dicrov.core.theme.TextSecondary
import com.Kiwdidi6.Dicrov.domain.model.ClubRank
import com.Kiwdidi6.Dicrov.domain.model.ReelSpeed
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol
import com.Kiwdidi6.Dicrov.domain.model.WinCelebration

fun SlotSymbol.spriteRes(): Int = when (this) {
    SlotSymbol.CHERRY -> R.drawable.sprite_1
    SlotSymbol.BAR -> R.drawable.sprite_2
    SlotSymbol.BELL -> R.drawable.sprite_3
    SlotSymbol.DIAMOND -> R.drawable.sprite_4
    SlotSymbol.SEVEN -> R.drawable.sprite_5
    SlotSymbol.WILD -> R.drawable.logo
}

fun SlotSymbol.nameRes(): Int = when (this) {
    SlotSymbol.CHERRY -> R.string.symbol_cherry
    SlotSymbol.BAR -> R.string.symbol_bar
    SlotSymbol.BELL -> R.string.symbol_bell
    SlotSymbol.DIAMOND -> R.string.symbol_diamond
    SlotSymbol.SEVEN -> R.string.symbol_seven
    SlotSymbol.WILD -> R.string.symbol_wild
}

fun SlotSymbol.accent(): Color = when (this) {
    SlotSymbol.CHERRY -> Danger
    SlotSymbol.BAR -> TextSecondary
    SlotSymbol.BELL -> GoldLight
    SlotSymbol.DIAMOND -> Success
    SlotSymbol.SEVEN -> Gold
    SlotSymbol.WILD -> GoldLight
}

fun ClubRank.nameRes(): Int = when (this) {
    ClubRank.GUEST -> R.string.rank_guest
    ClubRank.MEMBER -> R.string.rank_member
    ClubRank.REGULAR -> R.string.rank_regular
    ClubRank.PATRON -> R.string.rank_patron
    ClubRank.FOUNDER -> R.string.rank_founder
}

fun ClubRank.perkRes(): Int = when (this) {
    ClubRank.GUEST -> R.string.perk_guest
    ClubRank.MEMBER -> R.string.perk_member
    ClubRank.REGULAR -> R.string.perk_regular
    ClubRank.PATRON -> R.string.perk_patron
    ClubRank.FOUNDER -> R.string.perk_founder
}

fun ReelSpeed.labelRes(): Int = when (this) {
    ReelSpeed.RELAXED -> R.string.speed_relaxed
    ReelSpeed.STANDARD -> R.string.speed_standard
    ReelSpeed.FAST -> R.string.speed_fast
}

fun WinCelebration.labelRes(): Int = when (this) {
    WinCelebration.FULL -> R.string.celebration_full
    WinCelebration.SHORT -> R.string.celebration_short
}
