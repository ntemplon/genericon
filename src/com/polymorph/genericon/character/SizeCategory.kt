package com.polymorph.genericon.character

import com.polymorph.genericon.math.DieRoll

sealed class SizeCategory(val index: Int, val name: String) {

    object Fine: SizeCategory(1,"Fine")

    object Diminutive: SizeCategory(2,"Diminutive")

    object Tiny: SizeCategory(3,"Tiny")

    object Small: SizeCategory(4,"Small")

    object Medium: SizeCategory(5,"Medium")

    object Large: SizeCategory(6,"Large")

    object Huge: SizeCategory(7,"Huge")

    object Gargantuan: SizeCategory(8,"Gargantuan")

    object Colossal: SizeCategory(9,"Colossal")

}

interface Sized {
    val size: SizeCategory
}