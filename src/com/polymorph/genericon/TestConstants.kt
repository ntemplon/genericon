package com.polymorph.genericon

import com.polymorph.genericon.character.*
import com.polymorph.genericon.item.DamageProgression
import com.polymorph.genericon.item.WeaponCategory
import com.polymorph.genericon.math.DieRoll

object TestConstants {

    val dwarf = Race(SizeCategory.Medium, AttributeSet(
            strength = Attribute(0),
            dexterity = Attribute(0),
            constitution = Attribute(2),
            intelligence = Attribute(0),
            wisdom = Attribute(0),
            charisma = Attribute(-2)
    ))

    val dwarvenWaraxe = WeaponCategory(
            name = "Dwarven Waraxe",
            toHitModifier = DieRoll.constant(0),
            damageProgression = DamageProgression._1d10Medium,
            critPredicate = { it == 20 },
            critDamage = { baseDamage -> baseDamage + baseDamage + baseDamage }
    )

    val greatsword = WeaponCategory(
            name = "Greatsword",
            toHitModifier = DieRoll.constant(0),
            damageProgression = DamageProgression._2d6Medium,
            critPredicate = { it == 19 || it == 20 },
            critDamage = { baseDamage -> baseDamage + baseDamage }
    )

    val girtBlanston = Character(
            name = "Girt Blanston",
            race = dwarf,
            baseAttributes = AttributeSet(
                    strength = Attribute(16),
                    dexterity = Attribute(10),
                    constitution = Attribute(15),
                    intelligence = Attribute(10),
                    wisdom = Attribute(12),
                    charisma = Attribute(9)
            )
    )
}