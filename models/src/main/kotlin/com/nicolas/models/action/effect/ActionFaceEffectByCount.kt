package com.nicolas.models.action.effect

typealias ActionFaceEffectByCount = Map<Int, List<ActionFaceEffect>>

val ActionFaceEffectByCount?.size: Int
    get() = this?.size ?: 0
