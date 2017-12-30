package com.polymorph.genericon.io

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.polymorph.genericon.combat.DamageType
import com.polymorph.genericon.util.GenericonReflections
import com.polymorph.genericon.util.Option
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.reflect.full.createInstance

object Serialization {

    val gson: Gson = GsonBuilder()
            .setPrettyPrinting()
            .apply {
                GenericonReflections.reflections
                        .getSubTypesOf(SerializationSupport::class.java)
                        .map { cls -> cls.kotlin }
                        .map { kcls -> kcls.objectInstance ?: kcls.createInstance() }
                        .forEach { registerTypeAdapter(it.type, it) }
            }
            .create()


    inline fun <reified T> createType(): Type = (object : TypeToken<T>() {}).type

    inline fun <reified T> deserialize(path: Path): Option<T> = try {
        val text = Files.readAllLines(path).joinToString(System.lineSeparator())

        Option.some(gson.fromJson(text, createType<T>()))
    } catch (e: Exception) {
        Option.none()
    }

    inline fun <reified T> serialize(obj: T, path: Path) {
        val text = gson.toJson(obj, createType<T>())

        Files.write(path, text.toByteArray())
    }

    inline fun <reified T> fromJsonTree(json: JsonElement): T = gson.fromJson(json, createType<T>())

    inline fun <reified T> toJsonTree(obj: T): JsonElement = gson.toJsonTree(obj, createType<T>())

}


interface SerializationSupport<T> : JsonSerializer<T>, JsonDeserializer<T> {
    val type: Type
}