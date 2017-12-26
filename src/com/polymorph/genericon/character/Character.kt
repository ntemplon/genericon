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


    fun attackUsing(weapon: Weapon): Attack {
        val toHit = DieRoll.constant(baseAttackBonus + attributes.strength.modifier) + weapon.toHitModifier
        val damage = weapon.damage + attributes.strength.modifier

        return Attack(toHit,  damage, weapon.critPredicate, weapon.critDamage)
    }

}