package com.polymorph.genericon.combat

import com.polymorph.genericon.math.DieRoll

/**
 * @property toHitModifier The attack's to-hid modifier, not counting the standard mechanic (d20 or 3d6) roll
 * @property damage The attack's entire damage modifier
 * @property critPredicate A function that checks an integer result of the standard mechanic roll to determine if it is a crit.
 * @property critDamageFunction A function that accepts the normal attack damage as an argument and outputs critical hit damage.
 */
data class Attack(val toHitModifier: DieRoll,
                  val damage: DieRoll,
                  val critPredicate: (Int) -> Boolean,
                  private val critDamageFunction: (DieRoll) -> DieRoll) {

    val critDamage: DieRoll = this.critDamageFunction(this.damage)

}