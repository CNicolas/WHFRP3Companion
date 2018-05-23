package com.nicolas.models.player.effect

import com.nicolas.models.dice.Dice
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.skill.Skill
import com.nicolas.models.player.skill.Specialization
import java.io.Serializable

data class Effect(val name: String,

                  val characteristics: List<Characteristic>? = null,

                  val allSkills: Boolean = false,
                  val skills: List<Skill>? = null,
                  val specializations: List<Specialization>? = null,

                  val addedDices: List<Dice>? = null,
                  val removedDices: List<Dice>? = null,

                  val soak: Int? = null,
                  val defense: Int? = null) : Serializable