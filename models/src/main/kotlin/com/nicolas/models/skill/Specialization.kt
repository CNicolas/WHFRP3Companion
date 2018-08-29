package com.nicolas.models.skill

import com.nicolas.models.item.enums.WeaponType
import java.io.Serializable

data class Specialization(val name: String,
                          var isSpecialized: Boolean = false,
                          val weaponTypes: List<WeaponType>? = null) : Serializable