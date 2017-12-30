package com.polymorph.genericon.combat

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.polymorph.genericon.Identified
import com.polymorph.genericon.io.FileLocations
import com.polymorph.genericon.io.Serialization
import com.polymorph.genericon.io.SerializationSupport
import com.polymorph.genericon.util.Option
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.HashMap

class DamageType private constructor(val name: String, id: UUID): Identified(id) {


    companion object {

        private val damageTypesMutable: MutableMap<UUID, DamageType> = HashMap()


        val damageTypes: Iterable<DamageType>
            get() = damageTypesMutable.values


        fun lookup(id: UUID): DamageType? = damageTypesMutable[id]

        fun create(name: String, id: UUID = UUID.randomUUID()): DamageType {
            val damageType = DamageType(name, id)

            damageTypesMutable.put(damageType.id, damageType)

            return damageType
        }

        fun destroy(damageType: DamageType) {
            if (damageTypesMutable[damageType.id] == damageType) {
                damageTypesMutable.remove(damageType.id)
            }
        }

        fun read() {
            val fromFile = Serialization.deserialize<HashMap<UUID, DamageType>>(FileLocations.damageTypeFile)

            if (fromFile is Option.Some) {
                for((uuid, damageType) in fromFile.value) {
                    damageTypesMutable[uuid] = damageType
                }
            }
        }

        fun write() {
            Serialization.serialize(damageTypesMutable, FileLocations.damageTypeFile)
        }

    }

}


object DamageTypeSerializationSupport : SerializationSupport<DamageType> {

    private val nameKey: String = "name"
    private val idKey: String = "id"


    override val type: Type = Serialization.createType<DamageType>()


    override fun serialize(src: DamageType, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val root = JsonObject()

        root.add(idKey, Serialization.toJsonTree(src.id))
        root.add(nameKey, Serialization.toJsonTree(src.name))

        return root
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DamageType {
        val root = json.asJsonObject

        val id = Serialization.fromJsonTree<UUID>(root.get(idKey))
        val name = Serialization.fromJsonTree<String>(root.get(nameKey))

        return DamageType.create(name, id)
    }

}