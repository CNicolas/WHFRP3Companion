package com.nicolas.whfrp3database.player

import com.nicolas.whfrp3database.BuildConfig
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.item.Armor
import com.nicolas.whfrp3database.entities.player.playerLinked.item.Expandable
import com.nicolas.whfrp3database.entities.player.playerLinked.item.GenericItem
import com.nicolas.whfrp3database.entities.player.playerLinked.item.Weapon
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ArmorType.HELMET
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ArmorType.PLATE
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Quality.LOW
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Quality.NORMAL
import com.nicolas.whfrp3database.extensions.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, assetDir = "/src/test/assets")
class PlayerFacadeItemsTest : AbstractPlayerFacadeTest() {
    @Test
    fun should_update_items_of_a_player() {
        val playerName = "PlayerName"

        val player = playerFacade.add(Player(playerName))
        assertThat(player.name).isEqualTo(playerName)
        assertThat(player.items).isEmpty()
        assertThat(player.encumbrance).isZero()

        player.addItem(Weapon(name = "Sword", damage = 4, isEquipped = true, encumbrance = 3))

        val updatedPlayer = playerFacade.update(player)
        assertThat(updatedPlayer.name).isEqualTo(playerName)
        assertThat(updatedPlayer.items).isNotEmpty
        assertThat(updatedPlayer.items.size).isEqualTo(1)
        assertThat(updatedPlayer.items).isEqualTo(player.items)
        assertThat(updatedPlayer.items[0] is Weapon).isTrue()
        assertThat(updatedPlayer).isEqualToComparingFieldByField(player)
        assertThat(updatedPlayer.encumbrance).isEqualTo(3)

        val weapon = updatedPlayer.getWeapons()[0]
        assertThat(weapon.name).isEqualTo("Sword")
        assertThat(weapon.damage).isEqualTo(4)
        assertThat(weapon.isEquipped).isTrue()

        player.addItem(Armor(name = "Plates armor", subType = PLATE, isEquipped = false, encumbrance = 3, soak = 4))

        val updatedPlayer2 = playerFacade.update(player)
        assertThat(updatedPlayer.name).isEqualTo(playerName)
        assertThat(updatedPlayer.items).isNotEmpty
        assertThat(updatedPlayer.items.size).isEqualTo(2)
        assertThat(updatedPlayer.items).isEqualTo(player.items)
        assertThat(updatedPlayer.items[0] is Weapon).isTrue()
        assertThat(updatedPlayer.items[1] is Armor).isTrue()
        assertThat(updatedPlayer).isEqualToComparingFieldByField(player)
        assertThat(updatedPlayer.encumbrance).isEqualTo(6)

        val sameWeapon = updatedPlayer2.getWeaponByName("Sword")
        assertThat(sameWeapon).isNotNull()
        assertThat(sameWeapon!!.name).isEqualTo("Sword")
        assertThat(sameWeapon.damage).isEqualTo(4)
        assertThat(sameWeapon.isEquipped).isTrue()
        val armor = updatedPlayer2.getArmors()[0]
        assertThat(armor.name).isEqualTo("Plates armor")
        assertThat(armor.encumbrance).isEqualTo(3)
        assertThat(armor.subType).isEqualTo(PLATE)
        assertThat(armor.isEquipped).isEqualTo(false)
        assertThat(armor.soak).isEqualTo(4)
        assertThat(armor.defense).isEqualTo(0)
    }

    @Test
    fun should_update_field_of_item_of_a_player() {
        val player = playerFacade.add(Player("John", items = listOf(GenericItem(name = "Rope"))))
        assertThat(player.name).isEqualTo("John")
        assertThat(player.items.size).isEqualTo(1)
        assertThat(player.items[0] is GenericItem).isTrue()
        val genericItem = player.getGenericItems()[0]
        assertThat(genericItem.name).isEqualTo("Rope")
        assertThat(genericItem.quality).isEqualTo(NORMAL)

        genericItem.quality = LOW

        val updatedPlayer = playerFacade.update(player)
        assertThat(updatedPlayer.name).isEqualTo("John")
        assertThat(updatedPlayer.items.size).isEqualTo(1)
        assertThat(updatedPlayer.items[0] is GenericItem).isTrue()
        val newItem = updatedPlayer.getGenericItems()[0]
        assertThat(newItem.quality).isEqualTo(LOW)
    }

    @Test
    fun should_update_name_of_item_of_a_player() {
        val player = playerFacade.add(
                Player("John", items = listOf(Armor(name = "Helmet", soak = 2, subType = HELMET, defense = 1)))
        )
        assertThat(player.name).isEqualTo("John")
        assertThat(player.items.size).isEqualTo(1)
        assertThat(player.items[0] is Armor).isTrue()
        val armor = player.items[0] as Armor
        assertThat(armor.name).isEqualTo("Helmet")
        assertThat(armor.soak).isEqualTo(2)
        assertThat(armor.defense).isEqualTo(1)
        assertThat(armor.subType).isEqualTo(HELMET)

        armor.name = "Shield"

        val updatedPlayer = playerFacade.update(player)
        assertThat(updatedPlayer.name).isEqualTo("John")
        assertThat(updatedPlayer.items.size).isEqualTo(1)
        assertThat(updatedPlayer.items[0] is Armor).isTrue()

        val newWeapon = updatedPlayer.getArmors()[0]
        assertThat(newWeapon.name).isEqualTo("Shield")
        assertThat(armor.soak).isEqualTo(2)
        assertThat(armor.defense).isEqualTo(1)
        assertThat(armor.subType).isEqualTo(HELMET)
    }

    @Test
    fun should_delete_items_of_player_then_player() {
        var player = playerFacade.add(Player("John", items = listOf(Weapon(name = "Sword", damage = 4, criticalLevel = 3))))
        assertThat(player.items.size).isEqualTo(1)

        player.removeAllItems()
        val updatedPlayer = playerFacade.update(player)
        assertThat(updatedPlayer.items).isEmpty()

        playerFacade.deletePlayer(player)
        assertThat(playerFacade.findAll()).isEmpty()

        player = playerFacade.add(Player("John", items = listOf(Weapon(name = "Sword", damage = 4, criticalLevel = 3))))
        assertThat(player.items.size).isEqualTo(1)
        playerFacade.deletePlayer("John")
        assertThat(playerFacade.findAll()).isEmpty()
    }

    @Test
    fun should_delete_an_item_of_player() {
        val player = playerFacade.add(Player("John", items = listOf(Expandable(name = "Potion", uses = 1))))
        assertThat(player.items.size).isEqualTo(1)
        assertThat(player.items[0] is Expandable).isTrue()
        assertThat(player.items[0].name).isEqualTo("Potion")

        val expandable = player.getExpandableByName("Potion")!!
        player.removeItem(expandable)
        assertThat(player.items.size).isEqualTo(0)

        playerFacade.update(player)

        assertThat(player.items.size).isEqualTo(0)
    }
}