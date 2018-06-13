package com.nicolas.models.effect

import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.skill.Skill
import com.nicolas.models.skill.Specialization
import java.io.Serializable

data class Trigger(val characteristics: List<Characteristic>? = null,
                   val skills: List<Skill>? = null,
                   val specializations: List<Specialization>? = null,

                   val unknown: Boolean = false,
                   val always: Boolean = false,
                   val everyRoll: Boolean = false,
                   val everyCharacteristic: Boolean = false,
                   val everySkill: Boolean = false,
                   val everyItem: Boolean = false,
                   val everyWeapon: Boolean = false,
                   val damageDealt: Boolean = false,
                   val damageTaken: Boolean = false,
                   val beingAttacked: Boolean = false,
                   val activeDefense: Boolean = false,
                   val maneuver: Boolean = false,
                   val engaged: Boolean = false,
                   val initiativeRoll: Boolean = false,
                   val turnStart: Boolean = false,
                   val turnEnd: Boolean = false,
                   val exhaustion: Boolean = false,
                   val stress: Boolean = false) : Serializable

