package com.nicolas.models.action.effect

import com.nicolas.models.dice.Face

typealias ActionEffect = Triple<Face, Int, ActionFaceEffect>

val ActionEffect.face: Face
    get() = first
val ActionEffect.faceCount: Int
    get() = second
val ActionEffect.effect: ActionFaceEffect
    get() = third