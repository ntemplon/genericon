package com.polymorph.genericon.item

import com.polymorph.genericon.character.SizeCategory
import com.polymorph.genericon.math.DieRoll
import com.polymorph.genericon.math.GameNumeric


data class Weapon(
        val name: String,
        val sizeOfWeilder: SizeCategory,
        val toHitModifier: GameNumeric,
        val damage: GameNumeric,
        val critPredicate: (Int) -> Boolean,
        val critDamage: (GameNumeric) -> GameNumeric,
        val properties: List<WeaponProperty>,
        val type: WeaponType
) {

    fun withProperty(property: WeaponProperty): Weapon = property.apply(this)

}


data class WeaponType(
        val name: String,
        val sizeOfWeilder: SizeCategory,
        val toHitModifier: GameNumeric,
        val damage: GameNumeric,
        val critPredicate: (Int) -> Boolean,
        val critDamage: (GameNumeric) -> GameNumeric,
        val category: WeaponCategory
) {

    fun instance(): Weapon = Weapon(
            name = this.name,
            sizeOfWeilder = this.sizeOfWeilder,
            toHitModifier = this.toHitModifier,
            damage = this.damage,
            critPredicate = this.critPredicate,
            critDamage = this.critDamage,
            properties = listOf(),
            type = this
    )

}


data class WeaponCategory(val name: String, val toHitModifier: GameNumeric, val damageProgression: Map<SizeCategory, GameNumeric>, val critPredicate: (Int) -> Boolean, val critDamage: (GameNumeric) -> GameNumeric) {

    fun forSize(size: SizeCategory): WeaponType = WeaponType(
            name = "${this.name} (${size.name})",
            sizeOfWeilder = size,
            toHitModifier = toHitModifier,
            damage = damageProgression[size]!!, // If the value isn't in the lookup, crash - this is a big problem
            critPredicate = critPredicate,
            critDamage = critDamage,
            category = this
    )

}

object DamageProgression {

    val _1d4Medium = listOf(
            SizeCategory.Fine to DieRoll.constant(0),
            SizeCategory.Diminutive to DieRoll.constant(1),
            SizeCategory.Tiny to DieRoll._1d2,
            SizeCategory.Small to DieRoll._1d3,
            SizeCategory.Medium to DieRoll._1d4,
            SizeCategory.Large to DieRoll._1d6,
            SizeCategory.Huge to DieRoll._1d8,
            SizeCategory.Gargantuan to DieRoll._2d6,
            SizeCategory.Colossal to DieRoll._3d6
    ).toMap()

    val _1d6Medium = listOf(
            SizeCategory.Fine to DieRoll.constant(1),
            SizeCategory.Diminutive to DieRoll._1d2,
            SizeCategory.Tiny to DieRoll._1d3,
            SizeCategory.Small to DieRoll._1d4,
            SizeCategory.Medium to DieRoll._1d6,
            SizeCategory.Large to DieRoll._1d8,
            SizeCategory.Huge to DieRoll._2d6,
            SizeCategory.Gargantuan to DieRoll._3d6,
            SizeCategory.Colossal to DieRoll._4d6
    ).toMap()

    val _1d8Medium = listOf(
            SizeCategory.Fine to DieRoll._1d2,
            SizeCategory.Diminutive to DieRoll._1d3,
            SizeCategory.Tiny to DieRoll._1d4,
            SizeCategory.Small to DieRoll._1d6,
            SizeCategory.Medium to DieRoll._1d8,
            SizeCategory.Large to DieRoll._2d6,
            SizeCategory.Huge to DieRoll._3d6,
            SizeCategory.Gargantuan to DieRoll._4d6,
            SizeCategory.Colossal to DieRoll._6d6
    ).toMap()

    val _1d10Medium = listOf(
            SizeCategory.Fine to DieRoll._1d3,
            SizeCategory.Diminutive to DieRoll._1d4,
            SizeCategory.Tiny to DieRoll._1d6,
            SizeCategory.Small to DieRoll._1d8,
            SizeCategory.Medium to DieRoll._1d10,
            SizeCategory.Large to DieRoll._2d8,
            SizeCategory.Huge to DieRoll._3d8,
            SizeCategory.Gargantuan to DieRoll._4d8,
            SizeCategory.Colossal to DieRoll._6d8
    ).toMap()

    val _1d12Medium = listOf(
            SizeCategory.Fine to DieRoll._1d4,
            SizeCategory.Diminutive to DieRoll._1d6,
            SizeCategory.Tiny to DieRoll._1d8,
            SizeCategory.Small to DieRoll._1d10,
            SizeCategory.Medium to DieRoll._1d12,
            SizeCategory.Large to DieRoll._3d6,
            SizeCategory.Huge to DieRoll._4d6,
            SizeCategory.Gargantuan to DieRoll._6d6,
            SizeCategory.Colossal to DieRoll._8d6
    ).toMap()

    val _2d6Medium = listOf(
            SizeCategory.Fine to DieRoll._1d4,
            SizeCategory.Diminutive to DieRoll._1d6,
            SizeCategory.Tiny to DieRoll._1d8,
            SizeCategory.Small to DieRoll._1d10,
            SizeCategory.Medium to DieRoll._2d6,
            SizeCategory.Large to DieRoll._3d6,
            SizeCategory.Huge to DieRoll._4d6,
            SizeCategory.Gargantuan to DieRoll._6d6,
            SizeCategory.Colossal to DieRoll._8d6
    ).toMap()
}


class WeaponProperty(val name: String, private val applyFunction: (Weapon) -> Weapon, private val removeFunction: (Weapon) -> Weapon) {

    fun apply(weapon: Weapon): Weapon {
        val modifiedWeapon = applyFunction.invoke(weapon)
        return modifiedWeapon.copy(properties = listOf(*modifiedWeapon.properties.toTypedArray(), this))
    }

    fun remove(weapon: Weapon): Weapon {
        assert(weapon.properties.contains(this))

        val modifiedWeapon = removeFunction.invoke(weapon)
        return modifiedWeapon.copy(properties = modifiedWeapon.properties.filterNot { it == this })
    }

}