package com.nicolas.whfrp3companion.shared.enums

import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.entities.player.enums.Race

internal val Race.labelId: Int
    get() = when (this) {
        Race.HUMAN -> R.string.human
        Race.DWARF -> R.string.dwarf
        Race.HIGH_ELF -> R.string.high_elf
        Race.WOOD_ELF -> R.string.wood_elf
    }
