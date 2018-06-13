package com.nicolas.models.effect

import com.nicolas.models.dice.DiceType
import java.io.Serializable

data class Effect(val name: String,
                  val trigger: Trigger = Trigger(unknown = true),
                  val duration: EffectDuration = EffectDuration.LINGERING,
                  val description: String = "",

                  val addedDices: List<DiceType>? = null,
                  val removedDices: List<DiceType>? = null,

                  val soak: Int? = null,
                  val defense: Int? = null,
                  val damageModifier: Int? = null,
                  val conservativeModifier: Int? = null,
                  val recklessModifier: Int? = null,
                  val cooldown: Int? = null,
                  val stress: Int? = null,
                  val exhaustion: Int? = null,

                  val counterEffectName: String? = null) : Serializable