package com.polymorph.genericon.character

import com.polymorph.genericon.combat.Attack
import com.polymorph.genericon.item.Weapon
import com.polymorph.genericon.item.WeaponType
import com.polymorph.genericon.math.DieRoll

class Character(
        val name: String,
        val race: Race,
        val baseAttributes: AttributeSet
): Sized by race
{

    val attributes
        get() = baseAttributes.combineWith(race.attributeAdjustments)

    // TODO: Remove constants
    val baseAttackBonus: Int = 4

}