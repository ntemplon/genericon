package com.polymorph.genericon

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.polymorph.genericon.io.Serialization
import com.polymorph.genericon.io.SerializationSupport
import java.lang.reflect.Type
import java.util.*

open class Identified(val id: UUID = UUID.randomUUID())


object UUIDSerializationSupport : SerializationSupport<UUID> {

    override val type: Type = Serialization.createType<UUID>()


    override fun serialize(src: UUID, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toString())
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): UUID {
        return UUID.fromString(json.asString)
    }

}