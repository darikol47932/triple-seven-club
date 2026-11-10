package com.Kiwdidi6.Dicrov.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L

    const val BET = 10
    const val STARTING_CHIPS = 1000
    const val TOP_UP_CHIPS = 200

    const val REELS = 3
    const val ROWS = 3
    const val STRIP_SIZE = 81

    const val SHARDS_PER_SYMBOL = 12
    const val CHARGE_CAP = 10
    const val CHARGE_CAP_PATRON = 8
    const val BIG_WIN_MULTIPLE = 15
    const val PITY_LOSS_STREAK = 5
    const val PITY_WILD_BOOST = 1.4f
    const val FEATURED_WEIGHT_BOOST = 1.5f

    const val STOP_BASE_MS = 900
    const val STOP_STAGGER_MS = 340
    const val EVALUATE_HOLD_MS = 180L
    const val WIN_FLASH_MS = 320L
    const val BIG_WIN_FULL_MS = 1200L
    const val BIG_WIN_SHORT_MS = 400L

    const val STRIP_SEED_A = 7919
    const val STRIP_SEED_B = 104729
    const val LAYOUT_ATTEMPTS = 20
}
