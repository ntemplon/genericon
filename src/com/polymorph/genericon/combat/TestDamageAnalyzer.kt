package com.polymorph.genericon.combat

class TestDamageAnalyzer(val baseAttack: Attack, val defenses: Defenses, val powerAttackMultiplier: Int = 1) {

    fun averageDamage(powerAttack: Int = 0): Double {
        val adjustedAttack = baseAttack.copy(
                toHitModifier = baseAttack.toHitModifier - powerAttack,
                damage = baseAttack.damage + (powerAttack * powerAttackMultiplier)
        )

        return CombatSystem.activeSystem.resolveAverage(adjustedAttack, defenses).averageDamage
    }

    fun optimumPowerAttack(maximumPowerAttack: Int): PowerAttackOptimizationResult = (0..maximumPowerAttack)
            .map { it to this.averageDamage(it) }
            .sortedByDescending { it.second }
            .map { PowerAttackOptimizationResult(it.first, it.second) }
            .first()

}

data class PowerAttackOptimizationResult(val powerAttack: Int, val damage: Double)