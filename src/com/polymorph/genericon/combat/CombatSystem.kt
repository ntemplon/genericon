package com.polymorph.genericon.combat

import com.polymorph.genericon.math.DieRoll

abstract class CombatSystem {

    abstract val name: String
    abstract val coreRoll: DieRoll


    abstract fun resolve(attack: Attack, defenses: Defenses): AttackResult
    abstract fun resolveAverage(attack: Attack, defenses: Defenses): AverageAttackResult


    data class AttackResult(val hit: Boolean, val damage: Int, val crit: Boolean)
    data class AverageAttackResult(val hitFraction: Double, val averageDamage: Double, val critRate: Double)

    companion object {
        val activeSystem: CombatSystem = DnD35CombatSystem
    }

}

object DnD35CombatSystem: CombatSystem() {

    override val name: String = "D&D 3.5 Combat System"
    override val coreRoll: DieRoll = DieRoll._1d20


    override fun resolve(attack: Attack, defenses: Defenses): AttackResult = this.resolveWithKnownRolls(attack, defenses, this.coreRoll.roll(), attack.toHitModifier.roll(), attack.damage.roll(), attack.critDamage.roll())

    override fun resolveAverage(attack: Attack, defenses: Defenses): AverageAttackResult {
        var hitFraction: Double = 0.0
        var averageDamage: Double = 0.0
        var critRate: Double = 0.0

        for((coreResult, coreProb) in this.coreRoll.probabilities) {
            for((toHitResult, toHitProb) in attack.toHitModifier.probabilities) {
                val compositeProb = coreProb * toHitProb
                val result = this.resolveWithKnownRolls(attack, defenses, coreResult, toHitResult, attack.damage.roll(), attack.critDamage.roll())

                if (result.hit) {
                    hitFraction += compositeProb

                    if (result.crit) {
                        averageDamage += attack.critDamage.average * compositeProb
                        critRate += compositeProb
                    } else {
                        averageDamage += attack.damage.average * compositeProb
                    }
                }
            }
        }

        return AverageAttackResult(hitFraction, averageDamage, critRate)
    }


    private fun resolveWithKnownRolls(attack: Attack, defenses: Defenses, d20Roll: Int, toHitModifierRoll: Int, damageRoll: Int, critDamageRoll: Int): AttackResult {
        val autoMiss = d20Roll == 1
        val autoHit = d20Roll == 20

        val hit = autoHit || (d20Roll + toHitModifierRoll >= defenses.armorClass && !autoMiss)

        return if (hit) {
            if (attack.critPredicate.invoke(d20Roll)) {
                AttackResult(true, critDamageRoll, true)
            } else {
                AttackResult(true, damageRoll, false)
            }
        } else {
            AttackResult(false, damageRoll, false)
        }
    }

}