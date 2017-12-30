package com.polymorph.genericon

inline fun <reified T> List<T>.append(item: T): List<T> = listOf(*this.toTypedArray(), item)
inline fun <reified T> List<T>.append(items: List<T>): List<T> = listOf(*this.toTypedArray(), *items.toTypedArray())