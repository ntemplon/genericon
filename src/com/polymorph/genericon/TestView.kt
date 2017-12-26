package com.polymorph.genericon

import com.polymorph.genericon.character.SizeCategory
import com.polymorph.genericon.combat.CombatSystem
import com.polymorph.genericon.combat.Defenses
import com.polymorph.genericon.combat.TestDamageAnalyzer
import com.polymorph.genericon.item.WeaponCategory
import com.polymorph.genericon.item.WeaponProperties
import javafx.scene.chart.NumberAxis
import tornadofx.*

class TestView : View("Test View") {
    override val root = borderpane {
        center {
            linechart("Power Attack Analysis", NumberAxis(), NumberAxis()) {
                xAxis.label = "Opponent AC"
                yAxis.label = "Damage Dealt with Optimum Power Attack"

                val pc = TestConstants.girtBlanston
                val maxPowerAttack = pc.baseAttackBonus

                val masterworkDwarvenWaraxe = TestConstants.dwarvenWaraxe
                        .forSize(SizeCategory.Medium)
                        .instance()
                        .withProperty(WeaponProperties.masterwork)

                val masterworkGreatsword = TestConstants.greatsword
                        .forSize(SizeCategory.Medium)
                        .instance()
                        .withProperty(WeaponProperties.masterwork)

                series("Girt - Dwarven Waraxe - Optimum Power Attack") {
                    val attack = pc.attackUsing(masterworkDwarvenWaraxe)

                    for(ac in 8..25) {
                        val analyzer = TestDamageAnalyzer(attack, Defenses(ac))
                        data(ac, analyzer.optimumPowerAttack(maxPowerAttack).damage)
                    }
                }

                series("Girt - Greatsword - Optimum Power Attack") {
                    val attack = pc.attackUsing(masterworkGreatsword)

                    for(ac in 8..25) {
                        val analyzer = TestDamageAnalyzer(attack, Defenses(ac), powerAttackMultiplier = 2)
                        data(ac, analyzer.optimumPowerAttack(maxPowerAttack).damage)
                    }
                }

                series("Girt - Dwarven Waraxe") {
                    val attack = pc.attackUsing(masterworkDwarvenWaraxe)

                    for(ac in 8..25) {
                        data(ac, CombatSystem.activeSystem.resolveAverage(attack, Defenses(ac)).averageDamage)
                    }
                }

                series("Girt - Greatsword") {
                    val attack = pc.attackUsing(masterworkGreatsword)

                    for(ac in 8..25) {
                        data(ac, CombatSystem.activeSystem.resolveAverage(attack, Defenses(ac)).averageDamage)
                    }
                }
            }
        }
    }
}
