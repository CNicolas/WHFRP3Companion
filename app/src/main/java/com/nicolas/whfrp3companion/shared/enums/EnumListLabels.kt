package com.nicolas.whfrp3companion.shared.enums

import android.content.Context
import com.nicolas.models.action.ActionType
import com.nicolas.models.action.Trait
import com.nicolas.models.item.enums.ArmorType
import com.nicolas.models.item.enums.Quality
import com.nicolas.models.item.enums.Range
import com.nicolas.models.item.enums.WeaponType
import com.nicolas.models.talent.TalentType
import java.text.Normalizer

internal fun Array<Quality>.labels(context: Context): List<String> = map { context.getString(it.labelId) }
internal fun Array<Range>.labels(context: Context): List<String> = map { context.getString(it.labelId) }

// region ArmorType

internal fun Array<ArmorType>.labels(context: Context): List<String> = map { context.getString(it.labelId) }

internal fun Array<ArmorType>.sortedAndLabels(context: Context): Pair<List<ArmorType>, List<String>> {
    val sortedLabels = labels(context).sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) {
        Normalizer.normalize(it, Normalizer.Form.NFD)
    })

    val sortedArmorTypes = sortedLabels.mapNotNull { label ->
        find { label == context.getString(it.labelId) }
    }

    return sortedArmorTypes to sortedLabels
}

// endregion

// region WeaponType

internal fun Array<WeaponType>.labels(context: Context): List<String> = map { context.getString(it.labelId) }

internal fun Array<WeaponType>.sortedAndLabels(context: Context): Pair<List<WeaponType>, List<String>> {
    val sortedLabels = labels(context).sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) {
        Normalizer.normalize(it, Normalizer.Form.NFD)
    })

    val sortedWeaponTypes = sortedLabels.mapNotNull { label ->
        find { label == context.getString(it.labelId) }
    }

    return sortedWeaponTypes to sortedLabels
}

// endregion

// region TalentType

internal fun Array<TalentType>.labels(context: Context): List<String> = map { context.getString(it.labelId) }

internal fun Array<TalentType>.sortedAndLabels(context: Context): Pair<List<TalentType>, List<String>> {
    val sortedLabels = labels(context).sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) {
        Normalizer.normalize(it, Normalizer.Form.NFD)
    })

    val sortedTalentTypes = sortedLabels.mapNotNull { label ->
        find { label == context.getString(it.labelId) }
    }

    return sortedTalentTypes to sortedLabels
}

// endregion

// region ActionType

internal fun Array<ActionType>.labels(context: Context): List<String> = map { context.getString(it.labelId) }

internal fun Array<ActionType>.sortedAndLabels(context: Context): Pair<List<ActionType>, List<String>> {
    val sortedLabels = labels(context).sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) {
        Normalizer.normalize(it, Normalizer.Form.NFD)
    })

    val sortedActionTypes = sortedLabels.mapNotNull { label ->
        find { label == context.getString(it.labelId) }
    }

    return sortedActionTypes to sortedLabels
}

// endregion

// region Trait

internal fun Array<Trait>.labels(context: Context): List<String> = map { context.getString(it.labelId) }

internal fun Array<Trait>.sortedAndLabels(context: Context): Pair<List<Trait>, List<String>> {
    val sortedLabels = labels(context).sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) {
        Normalizer.normalize(it, Normalizer.Form.NFD)
    })

    val sortedTraits = sortedLabels.mapNotNull { label ->
        find { label == context.getString(it.labelId) }
    }

    return sortedTraits to sortedLabels
}

// endregion