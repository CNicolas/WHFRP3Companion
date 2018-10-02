package com.nicolas.whfrp3companion.shared.viewModifications

import android.text.style.ImageSpan
import com.nicolas.whfrp3companion.R

enum class TextIcon(val drawableId: Int, val alignment: Int) {
    MISFORTUNE_DICE(R.drawable.ic_misfortune_dice, ImageSpan.ALIGN_BASELINE),
    FORTUNE_DICE(R.drawable.ic_fortune_dice, ImageSpan.ALIGN_BASELINE),
    EXPERTISE_DICE(R.drawable.ic_expertise_dice, ImageSpan.ALIGN_BASELINE),
    CHARACTERISTIC_DICE(R.drawable.ic_characteristic_dice, ImageSpan.ALIGN_BASELINE),
    CONSERVATIVE_DICE(R.drawable.ic_conservative_dice, ImageSpan.ALIGN_BASELINE),
    RECKLESS_DICE(R.drawable.ic_reckless_dice, ImageSpan.ALIGN_BASELINE),
    CHALLENGE_DICE(R.drawable.ic_challenge_dice, ImageSpan.ALIGN_BASELINE),

    BOON_FACE(R.drawable.ic_boon_black_16, ImageSpan.ALIGN_BOTTOM),
    BANE_FACE(R.drawable.ic_bane_black_16, ImageSpan.ALIGN_BOTTOM),
    SUCCESS_FACE(R.drawable.ic_success_black_16, ImageSpan.ALIGN_BOTTOM),
    DELAY_FACE(R.drawable.ic_delay_black_16, ImageSpan.ALIGN_BOTTOM),
    EXHAUSTION_FACE(R.drawable.ic_exhaustion_16dp, ImageSpan.ALIGN_BOTTOM),
    FAILURE_FACE(R.drawable.ic_failure_black_16, ImageSpan.ALIGN_BOTTOM),
    SIGMAR_FACE(R.drawable.ic_sigmar_black_16, ImageSpan.ALIGN_BOTTOM),
    CHAOS_FACE(R.drawable.ic_chaos_black_16, ImageSpan.ALIGN_BOTTOM),

    EXHAUSTION(R.drawable.ic_exhaustion_16dp, ImageSpan.ALIGN_BOTTOM),
    STRESS(R.drawable.ic_stress_16dp, ImageSpan.ALIGN_BOTTOM),

    P(R.drawable.circle_conservative, ImageSpan.ALIGN_BASELINE),
    N(R.drawable.circle_neutral, ImageSpan.ALIGN_BASELINE),
    T(R.drawable.circle_reckless, ImageSpan.ALIGN_BASELINE)
}