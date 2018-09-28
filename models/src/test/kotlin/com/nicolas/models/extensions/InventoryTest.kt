package com.nicolas.models.extensions

import com.nicolas.models.item.Expandable
import com.nicolas.models.item.Weapon
import com.nicolas.models.player.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class InventoryTest {
    @Test
    fun should_have_encumbrance_of_2() {
        val player = Player(name = "PlayerName", items = listOf(Weapon(name = "Baton", encumbrance = 2)))

        assertThat(player.encumbrance).isEqualTo(2)
    }

    @Test
    fun should_have_encumbrance_increased_when_adding_item() {
        val player = Player(name = "PlayerName", items = listOf(Weapon(name = "Baton", encumbrance = 2)))

        assertThat(player.items.size).isEqualTo(1)
        assertThat(player.encumbrance).isEqualTo(2)
        assertThat(player.getWeaponByName("Baton")).isNotNull

        player.addItem(Expandable(name = "Potion", quantity = 1, uses = 2, encumbrance = 1))
        assertThat(player.items.size).isEqualTo(2)
        assertThat(player.encumbrance).isEqualTo(3)
        assertThat(player.getWeaponByName("Baton")).isNotNull
        assertThat(player.getExpandableByName("Potion")).isNotNull
    }

    @Test
    fun should_have_encumbrance_decreased_when_removing_item() {
        val weapon = Weapon(name = "Baton", encumbrance = 2)
        val player = Player(name = "PlayerName", items = listOf(weapon))

        assertThat(player.items.size).isEqualTo(1)
        assertThat(player.encumbrance).isEqualTo(2)
        assertThat(player.getWeaponByName("Baton")).isNotNull

        player.removeItem(weapon)
        assertThat(player.items.size).isEqualTo(0)
        assertThat(player.encumbrance).isEqualTo(0)
        assertThat(player.getWeaponByName("Baton")).isNull()
    }
}