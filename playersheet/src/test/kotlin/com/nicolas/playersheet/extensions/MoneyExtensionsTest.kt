package com.nicolas.playersheet.extensions

import com.nicolas.models.player.Player
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowableOfType
import org.junit.Test

class MoneyExtensionsTest {
    @Test
    fun should_earn_money() {
        val player = Player(name = "PlayerName")

        assertThat(player.gold).isEqualTo(0)
        assertThat(player.silver).isEqualTo(0)
        assertThat(player.brass).isEqualTo(0)

        player.addMoney(1, 102, 26)

        assertThat(player.gold).isEqualTo(2)
        assertThat(player.silver).isEqualTo(3)
        assertThat(player.brass).isEqualTo(1)
    }

    @Test
    fun should_lose_money() {
        val player = Player(name = "PlayerName", gold = 10, silver = 50, brass = 20)

        assertThat(player.gold).isEqualTo(10)
        assertThat(player.silver).isEqualTo(50)
        assertThat(player.brass).isEqualTo(20)

        player.removeMoney(1, 60, 30)

        println("Gold = ${player.gold}, Silver = ${player.silver}, Brass = ${player.brass}")

        assertThat(player.gold).isEqualTo(8)
        assertThat(player.silver).isEqualTo(89)
        assertThat(player.brass).isEqualTo(15)
    }

    @Test
    fun should_throw_when_trying_to_lose_too_much_money() {
        val player = Player(name = "PlayerName")

        assertThat(player.gold).isEqualTo(0)
        assertThat(player.silver).isEqualTo(0)
        assertThat(player.brass).isEqualTo(0)

        val thrown = catchThrowableOfType({ player.removeMoney(0, 0, 1) }, IllegalArgumentException::class.java)
        assertThat(thrown).hasMessage("Not enough money")
    }
}