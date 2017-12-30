package com.polymorph.genericon.util

sealed class Option<T>(private val iterable: Iterable<T>): Iterable<T> by iterable {

    data class Some<T>(val value: T): Option<T>(listOf(value))

    class None<T>: Option<T>(listOf())


    companion object {
        fun <T> some(value: T): Option<T> = Some(value)
        fun <T> none(): Option<T> = None()
    }

}