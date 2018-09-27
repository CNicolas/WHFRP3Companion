package com.nicolas.whfrp3companion.shared.enums

import android.content.Context
import com.nicolas.models.item.enums.ArmorType
import com.nicolas.models.item.enums.Quality
import com.nicolas.models.item.enums.Range
import com.nicolas.models.item.enums.WeaponType

internal fun Array<Quality>.labels(context: Context): List<String> = map { context.getString(it.labelId) }

internal fun Array<ArmorType>.labels(context: Context): List<String> = map { context.getString(it.labelId) }

internal fun Array<ArmorType>.sortedAndLabels(context: Context): Pair<List<ArmorType>, List<String>> {
    val sortedLabels = labels(context).sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it })

    val sortedArmorTypes = sortedLabels.mapNotNull { label ->
        find { label == context.getString(it.labelId) }
    }

    return sortedArmorTypes to sortedLabels
}

internal fun Array<WeaponType>.labels(context: Context): List<String> = map { context.getString(it.labelId) }

internal fun Array<WeaponType>.sortedAndLabels(context: Context): Pair<List<WeaponType>, List<String>> {
    val sortedLabels = labels(context).sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it })

    val sortedWeaponTypes = sortedLabels.mapNotNull { label ->
        find { label == context.getString(it.labelId) }
    }

    return sortedWeaponTypes to sortedLabels
}

internal fun Array<Range>.labels(context: Context): List<String> = map { context.getString(it.labelId) }