package com.nicolas.models

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SharedOperatorsTest {
    @Test
    fun should_add_nullable_integers() {
        val one: Int? = 1
        val two: Int? = null
        val three: Int? = 2

        assertThat(one + one).isEqualTo(2)
        assertThat(one + two).isEqualTo(1)
        assertThat(one + three).isEqualTo(3)
        assertThat(two + one).isEqualTo(1)
        assertThat(two + two).isEqualTo(null)
        assertThat(two + three).isEqualTo(2)
        assertThat(three + one).isEqualTo(3)
        assertThat(three + two).isEqualTo(2)
        assertThat(three + three).isEqualTo(4)
    }
}