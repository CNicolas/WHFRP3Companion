package com.nicolas.models.player.effect

import com.nicolas.models.dice.Dice
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.item.enums.ArmorCategory
import com.nicolas.models.player.item.enums.ArmorType
import com.nicolas.models.player.item.enums.WeaponCategory
import com.nicolas.models.player.item.enums.WeaponType
import com.nicolas.models.player.skill.Skill
import com.nicolas.models.player.skill.Specialization
import java.io.Serializable

data class Effect(val name: String,

                  val allThrows: Boolean = false,

                  val allCharacteristics: Boolean = false,
                  val characteristics: List<Characteristic>? = null,

                  val allSkills: Boolean = false,
                  val skills: List<Skill>? = null,
                  val specializations: List<Specialization>? = null,

                  val allItems: Boolean = false,

                  val allWeapons: Boolean = false,
                  val weaponCategories: List<WeaponCategory>? = null,
                  val weaponTypes: List<WeaponType>? = null,

                  val allArmors: Boolean = false,
                  val armorCategories: List<ArmorCategory>? = null,
                  val armorTypes: List<ArmorType>? = null,

                  val addedDices: List<Dice>? = null,
                  val removedDices: List<Dice>? = null,

                  val soak: Int? = null,
                  val defense: Int? = null,

                  val damageDealtModifier: Int? = null,
                  val damageTakenModifier: Int? = null) : Serializable