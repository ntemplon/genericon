package com.polymorph.genericon.math

class DieRoll(frequencies: Map<Int, Int>): DiscreteDistribution<Int>(frequencies) {

    val average: Double by lazy { this.frequencies.map { (result, frequency) -> result * frequency }.sum().toDouble() / this.sumOfFrequencies }
    val isConstant: Boolean = this.frequencies.count() == 1


    fun probabilityToRollAtLeast(value: Int) = this.probabilityOf { it >= value }

    fun roll(): Int = this.sample()

    operator fun plus(other: DieRoll): DieRoll {
        val compositeFrequencies = this.frequencies
                .flatMap { (thisResult, thisFreq) -> other.frequencies
                                                .map { (otherResult, otherFreq) -> (thisResult + otherResult) to (thisFreq * otherFreq) }
                }
                .groupBy { (result, frequency) -> result }
                .map { it.key to it.value.map { (result, frequency) -> frequency }.sum() }
                .toMap()

        return DieRoll(compositeFrequencies)
    }

    operator fun plus(constant: Int): DieRoll = this + DieRoll.constant(constant)

    operator fun minus(constant: Int): DieRoll = this + (-1 * constant)


    companion object {
        val _1d2 = standardDie(2)
        val _1d3 = standardDie(3)
        val _1d4 = standardDie(4)
        val _1d6 = standardDie(6)
        val _1d8 = standardDie(8)
        val _1d10 = standardDie(10)
        val _1d12 = standardDie(12)
        val _1d20 = standardDie(20)

        val _2d6 = _1d6 + _1d6
        val _3d6 = _2d6 + _1d6
        val _4d6 = _3d6 + _1d6
        val _6d6 = _4d6 + _1d6 + _1d6
        val _8d6 = _6d6 + _1d6 + _1d6

        val _2d8 = _1d8 + _1d8
        val _3d8 = _2d8 + _1d8
        val _4d8 = _3d8 + _1d8
        val _6d8 = _4d8 + _1d8 + _1d8

        fun constant(value: Int): DieRoll = DieRoll(listOf(value to 1).toMap())

        fun standardDie(numSides: Int): DieRoll = DieRoll((1..numSides).map { it to 1 }.toMap())
    }

}