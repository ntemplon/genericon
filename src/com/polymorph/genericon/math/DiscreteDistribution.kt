package com.polymorph.genericon.math

import java.util.*

open class DiscreteDistribution<T>(val frequencies: Map<T, Int>) {

    private val partitioned: List<ValueWithCutoff<T>> = this.computePartitionedList()


    val probabilities: Map<T, Double> by lazy { this.frequencies.map { it.key to (it.value.toDouble() / sumOfFrequencies) }.toMap() }

    val sumOfFrequencies: Double = this.frequencies
            .map { it.value }
            .sum()
            .toDouble()


    fun sample(): T {
        val rand: Double = random.nextDouble()
        return partitioned
                .first { it.cutoff >= rand }
                .value
    }

    fun probabilityOf(item: T): Double = this.probabilities[item] ?: 0.0

    fun probabilityOf(predicate: (T) -> Boolean): Double {
        val compliantItems = this.frequencies.keys.filter { predicate.invoke(it) }
        val probabilities = compliantItems.map { this.probabilityOf(it) }
        return probabilities.sum()
    }


    private fun computePartitionedList(): List<ValueWithCutoff<T>> {
        var currentSum: Int = 0
        val list = mutableListOf<ValueWithCutoff<T>>()

        for((item, frequency) in this.frequencies) {
            currentSum += frequency
            list.add(ValueWithCutoff(item, frequency.toDouble() / sumOfFrequencies))
        }

        return list
    }


    companion object {
        private val random = Random()
    }


    data class ValueWithCutoff<out T>(val value: T, val cutoff: Double)

}