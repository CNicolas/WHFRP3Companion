package com.nicolas.models.action.effect

typealias ActionFaceEffectByCount = Map<Int, ActionFaceEffect>

val ActionFaceEffectByCount?.size: Int
    get() = this?.size ?: 0
