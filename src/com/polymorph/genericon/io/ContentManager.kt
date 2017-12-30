package com.polymorph.genericon.io

import com.polymorph.genericon.combat.DamageType

object ContentManager {

    fun read() {
        DamageType.read()
    }

    fun write() {
        DamageType.write()
    }

}