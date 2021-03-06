package com.nicolas.database.anko.tables.parsers

import com.google.gson.*
import com.nicolas.models.item.*
import com.nicolas.models.item.enums.ItemType.*
import java.lang.reflect.Type

class ItemDeserializer : JsonDeserializer<Item> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Item {
        val jsonObject = json.asJsonObject
        val prim = jsonObject.get("type") as JsonPrimitive
        val itemTypeString = prim.asString
        val clazz = getClassInstance(itemTypeString)

        return context.deserialize(jsonObject, clazz) ?: throw JsonParseException("Can not deserialize this object")
    }

    private fun getClassInstance(itemTypeString: String): Type {
        val type = valueOf(itemTypeString)

        return when (type) {
            ARMOR -> Armor::class.java
            EXPANDABLE -> Expandable::class.java
            GENERIC_ITEM -> GenericItem::class.java
            WEAPON -> Weapon::class.java
        }
    }


}