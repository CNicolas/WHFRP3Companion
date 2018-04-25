package com.nicolas.models.hand

import com.nicolas.models.NamedEntity

data class Hand(override var name: String,
                var characteristicDicesCount: Int = 0,
                var expertiseDicesCount: Int = 0,
                var fortuneDicesCount: Int = 0,
                var conservativeDicesCount: Int = 0,
                var recklessDicesCount: Int = 0,
                var challengeDicesCount: Int = 0,
                var misfortuneDicesCount: Int = 0,

                override val id: Int = -1) : NamedEntity