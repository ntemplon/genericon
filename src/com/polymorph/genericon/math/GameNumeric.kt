package com.polymorph.genericon.math

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.polymorph.genericon.Identified
import com.polymorph.genericon.append
import com.polymorph.genericon.io.Serialization
import com.polymorph.genericon.io.SerializationSupport
import tornadofx.observable
import java.lang.reflect.Type
import java.util.*

class GameNumeric(val components: List<NumericValue>) {

    // Each type mapped to all components of that type, sorted from largest to smallest by average roll
    val componentGroups by lazy {
        components
                .groupBy { it.type }
                .map { (type, components) -> type to components.sortedByDescending { it.dieRoll.average } }
                .toMap()
    }

    val activeComponents: Map<NumericCategory, List<NumericValue>>
        get() = componentGroups
                .map { (type, components) ->
                    if (type.stacks) {
                        type to components
                    } else {
                        type to listOf(components.first())
                    }
                }
                .toMap()

    val average: AverageNumericResult
        get() {
            val valueByType = activeComponents
                    .map { (type, components) -> type to components.map { it.dieRoll.average }.sum() }
                    .toMap()

            return AverageNumericResult(valueByType.values.sum(), valueByType)
        }


    fun add(numericValue: NumericValue) = GameNumeric(components.append(numericValue))

    fun add(numericValues: List<NumericValue>) = GameNumeric(components.append(numericValues))

    fun ignore(category: NumericCategory) = GameNumeric(components.filterNot { it.type == category })

    fun remove(numericValue: NumericValue) = GameNumeric(components.filterNot { it == numericValue })

    fun sample(): NumericResult {
        val valueByType = activeComponents
                .map { (type, components) -> type to components.map { it.dieRoll.roll() }.sum() }
                .toMap()

        return NumericResult(valueByType.values.sum(), valueByType)
    }


    data class NumericResult(val total: Int, val components: Map<NumericCategory, Int>)

    data class AverageNumericResult(val total: Double, val components: Map<NumericCategory, Double>)

}


class NumericCategory private constructor(name: String, stacks: Boolean, id: UUID, immutable: Boolean = false) : Identified(id) {

    var name: String = name
        set(value) {
            if (this.immutable) {
                throw IllegalStateException("Cannot attempt to modify the properties of an immutable category!")
            }

            field = value
        }

    var stacks: Boolean = stacks
        set(value) {
            if (this.immutable) {
                throw IllegalStateException("Cannot attempt to modify the properties of an immutable category!")
            }

            field = value
        }

    val immutable: Boolean = immutable
    val mutable: Boolean = !immutable


    override fun hashCode(): Int {
        var hash = 17

        hash = hash * 31 + name.hashCode()
        hash = hash * 31 + stacks.hashCode()
        hash = hash * 31 + id.hashCode()

        return hash
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        return if (other is NumericCategory) {
            (name == other.name
                    && stacks == other.stacks
                    && id == other.id)
        } else {
            false
        }
    }


    companion object {
        private val categoryLookupMutable = HashMap<UUID, NumericCategory>()
        private val gameMechanicName: String = "Mechanic"
        private val untypedName: String = "Untyped"

        val categoryLookup: Map<UUID, NumericCategory>
            get() {
                return categoryLookupMutable
            }

        val categories: List<NumericCategory>
            get() = categoryLookup.values.sortedBy { it.name }

        // The (fixed) game mechanic category.
        val gameMechanic: NumericCategory by lazy {
            val mechanicCategories = categoryLookup.values.filter { it.name.toUpperCase() == gameMechanicName.toUpperCase() }
            when (mechanicCategories.size) {
                0 -> NumericCategory.create(name = gameMechanicName, stacks = true, id = UUID.randomUUID(), immutable = true)
                1 -> mechanicCategories.first()
                else -> throw IllegalStateException("There is more than 1 game mechanic category!")
            }
        }

        // The (fixed) untyped category.
        val untyped: NumericCategory by lazy {
            val untypedCategories = categoryLookup.values.filter { it.name.toUpperCase() == untypedName.toUpperCase() }
            when (untypedCategories.size) {
                0 -> NumericCategory.create(name = untypedName, stacks = true, id = UUID.randomUUID(), immutable = true)
                1 -> untypedCategories.first()
                else -> throw IllegalStateException("There is more than 1 untyped category!")
            }
        }


        @JvmOverloads
        fun create(name: String = "Default", stacks: Boolean = false, id: UUID = UUID.randomUUID()) = create(name, stacks, id, false)


        private fun create(name: String, stacks: Boolean, id: UUID, immutable: Boolean): NumericCategory {
            val category = NumericCategory(name, stacks, id, immutable)

            categoryLookupMutable.put(category.id, category)

            return category
        }
    }

}


data class NumericValue(val dieRoll: DieRoll, val type: NumericCategory)


object NumericCategorySerializationSupport : SerializationSupport<NumericCategory> {

    private val nameKey: String = "name"
    private val stacksKey: String = "stacks"
    private val idKey: String = "id"

    override val type: Type = Serialization.createType<NumericCategory>()


    override fun serialize(src: NumericCategory, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val root = JsonObject()

        root.add(idKey, Serialization.toJsonTree(src.id))
        root.add(nameKey, Serialization.toJsonTree(src.name))
        root.add(stacksKey, Serialization.toJsonTree(src.stacks))

        return root
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): NumericCategory {
        val root = json.asJsonObject

        val id = Serialization.fromJsonTree<UUID>(root.get(idKey))
        val name = Serialization.fromJsonTree<String>(root.get(nameKey))
        val stacks = Serialization.fromJsonTree<Boolean>(root.get(stacksKey))

        return NumericCategory.create(name, stacks, id)
    }

}