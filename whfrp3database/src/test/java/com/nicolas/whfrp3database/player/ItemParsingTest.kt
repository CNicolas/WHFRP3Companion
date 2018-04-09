package com.nicolas.whfrp3database.player

import com.google.gson.GsonBuilder
import com.nicolas.whfrp3database.entities.player.playerLinked.item.Armor
import com.nicolas.whfrp3database.entities.player.playerLinked.item.Expandable
import com.nicolas.whfrp3database.entities.player.playerLinked.item.Item
import com.nicolas.whfrp3database.entities.player.playerLinked.item.Weapon
import com.nicolas.whfrp3database.tables.parsers.ItemDeserializer
import com.nicolas.whfrp3database.tables.parsers.genericType
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

class ItemParsingTest {
    private val armor = Armor("SampleName")
    private val weapon = Weapon("SampleName")
    private val expandable = Expandable("SampleName")
    private val serializedArmor = """{"type":"ARMOR","name":"SampleName","encumbrance":0,"quantity":1,"quality":"NORMAL","isEquipped":false,"subType":"LEATHER","soak":0,"defense":0}"""
    private val serializedWeapon = """{"type":"WEAPON","name":"SampleName","encumbrance":0,"quantity":1,"quality":"NORMAL","isEquipped":false,"subType":"SWORD","damage":0,"criticalLevel":0,"range":"ENGAGED"}"""

    private val gson = GsonBuilder()
            .registerTypeAdapter(Item::class.java, ItemDeserializer())
            .create()

    @Test
    fun should_serialize_armor() {
        val jsonArmor = gson.toJson(armor)

        assertThat(jsonArmor).isEqualTo(serializedArmor)
    }

    @Test
    fun should_serialize_list_of_armor() {
        val jsonListOfArmor = gson.toJson(listOf(armor))

        assertThat(jsonListOfArmor).isEqualTo("[$serializedArmor]")
    }


    @Test
    fun should_deserialize_armor() {
        val armor = gson.fromJson<Armor>(serializedArmor, genericType<Armor>())

        assertThat(armor.name).isEqualTo("SampleName")
    }

    @Test
    fun should_deserialize_item() {
        val armor = gson.fromJson<Item>(serializedArmor, genericType<Item>())

        assertThat(armor is Armor).isTrue()
        assertThat(armor.name).isEqualTo("SampleName")
    }

    @Test
    fun should_deserialize_list_of_armor() {
        val armors = gson.fromJson<List<Armor>>("[$serializedArmor]", genericType<List<Armor>>())

        assertThat(armors).isNotEmpty
        assertThat(armors.size).isEqualTo(1)
        assertThat(armors[0].name).isEqualTo("SampleName")
    }


    @Test
    fun should_serialize_list_of_items() {
        val jsonListOfItems = gson.toJson(listOf(armor, weapon))

        assertThat(jsonListOfItems).isEqualTo("[$serializedArmor,$serializedWeapon]")
    }

    @Test
    fun should_deserialize_list_of_items() {
        val items: List<Item> = gson.fromJson("[$serializedArmor, $serializedWeapon]", genericType<List<Item>>())

        assertThat(items).isNotEmpty
        assertThat(items.size).isEqualTo(2)

        val foundArmor = items[0]
        assertThat(foundArmor is Armor).isTrue()
        assertThat(foundArmor.name).isEqualTo("SampleName")

        val foundWeapon = items[1]
        assertThat(foundWeapon is Weapon).isTrue()
        assertThat(foundWeapon.name).isEqualTo("SampleName")
    }

    @Test
    fun should_json_and_get_afterwards() {
        val items = listOf(armor, weapon, expandable)
        val jsonString = gson.toJson(items)
        val newItems: List<Item> = gson.fromJson(jsonString, genericType<List<Item>>())

        val foundArmor = newItems[0]
        assertThat(foundArmor is Armor).isTrue()
        assertThat(foundArmor.name).isEqualTo("SampleName")

        val foundWeapon = newItems[1]
        assertThat(foundWeapon is Weapon).isTrue()
        assertThat(foundWeapon.name).isEqualTo("SampleName")

        val foundExpandable = newItems[2]
        assertThat(foundExpandable is Expandable).isTrue()
        assertThat(foundExpandable.name).isEqualTo("SampleName")
    }
}