package com.nicolas.whfrp3database.entities.hand

import com.nicolas.whfrp3database.entities.NamedEntity

data class Hand(override var name: String,
                var characteristicDicesCount: Int = 0,
                var expertiseDicesCount: Int = 0,
                var fortuneDicesCount: Int = 0,
                var conservativeDicesCount: Int = 0,
                var recklessDicesCount: Int = 0,
                var challengeDicesCount: Int = 0,
                var misfortuneDicesCount: Int = 0,

                override val id: Int = -1) : NamedEntity