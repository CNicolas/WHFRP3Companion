package com.nicolas.models.item

import com.nicolas.models.item.enums.ItemType
import com.nicolas.models.item.enums.Quality
import java.io.Serializable

interface Item : Serializable {
    var name: String

    var description: String?
    var encumbrance: Int
    var quantity: Int
    var quality: Quality
    var type: ItemType
}