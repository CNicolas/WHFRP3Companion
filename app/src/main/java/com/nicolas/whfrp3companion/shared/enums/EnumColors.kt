package com.nicolas.whfrp3companion.shared.enums

import com.nicolas.models.player.enums.Stance
import com.nicolas.models.player.enums.Stance.*
import com.nicolas.models.talent.TalentType
import com.nicolas.models.talent.TalentType.*
import com.nicolas.whfrp3companion.R

internal val TalentType.colorId: Int
    get() = when (this) {
        AFFINITY -> R.color.blue
        CAREER -> android.R.color.black
        FAITH -> R.color.colorPrimary
        ORDER -> R.color.violet
        REPUTATION -> R.color.orange
        TACTICS -> R.color.dark_red
        TRICK -> R.color.conservative
    }

internal val Stance.colorId: Int
    get() = when (this) {
        CONSERVATIVE -> R.color.conservative
        RECKLESS -> R.color.reckless
        NEUTRAL -> R.color.colorPrimaryDark
    }