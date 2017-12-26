package com.polymorph.genericon.character

class Attribute(val value: Int) {
    val modifier: Int
        get() = Math.floorDiv(value - 10, 2)
}

class AttributeSet(
        val strength: Attribute,
        val constitution: Attribute,
        val dexterity: Attribute,
        val intelligence: Attribute,
        val wisdom: Attribute,
        val charisma: Attribute): List<Attribute> by listOf(strength, constitution, dexterity, intelligence, wisdom, charisma) {

    fun combineWith(other: AttributeSet): AttributeSet = AttributeSet(
            strength = Attribute(this.strength.value + other.strength.value),
            dexterity = Attribute(this.dexterity.value + other.dexterity.value),
            constitution = Attribute(this.constitution.value + other.constitution.value),
            intelligence = Attribute(this.intelligence.value + other.intelligence.value),
            wisdom = Attribute(this.wisdom.value + other.wisdom.value),
            charisma = Attribute(this.charisma.value + other.charisma.value)
    )

}