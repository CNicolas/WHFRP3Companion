package com.nicolas.whfrp3companion.shared

import java.text.Normalizer

val String.normalized: String
    get() = Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+", "")