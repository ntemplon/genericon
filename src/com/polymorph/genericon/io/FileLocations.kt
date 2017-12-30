package com.polymorph.genericon.io

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.jvm.javaType

object FileLocations {

    val rootDirectory: Path = Paths.get(System.getProperty("user.home")).resolve(".genericon")
    val dataDirectory: Path = rootDirectory.resolve("data")

    val combatDirectory: Path = dataDirectory.resolve("combat")
    val weaponsDirectory: Path = combatDirectory.resolve("weapons")

    val damageTypeFile: Path = combatDirectory.resolve("damage_types.json")


    // Verify all paths exist
    init {
        val members = FileLocations::class.members
        val pathMembers = members.filter { member -> member.returnType.isSubtypeOf(Path::class.createType()) }
        val files = pathMembers.map { member -> member.call(FileLocations) }
                .mapNotNull { file -> file as? Path }

        files
                .filterNot { Files.exists(it.parent) }
                .forEach { Files.createDirectories(it.parent) }
    }

}