package com.polymorph.genericon

import javafx.scene.chart.NumberAxis
import tornadofx.*

class TestView : View("My View") {
    override val root = borderpane {
        center {
            linechart("AC vs Level", NumberAxis(), NumberAxis()) {
                multiseries("Fighter AC", "Monster AC") {
                    for(level in 1..20) {
                        val fighterAC = if (level <=2) {
                            18
                        } else if (level <= 4) {
                            19
                        } else {
                            20
                        }

                        val monsterAC = if (level <= 3) {
                            13
                        } else if (level <= 4) {
                            14
                        } else if (level <= 7) {
                            15
                        } else if (level <= 9) {
                            16
                        } else if (level <= 12) {
                            17
                        } else if (level <= 16) {
                            18
                        } else {
                            19
                        }

                        data(level, fighterAC, monsterAC)
                    }
                }
            }
        }
    }
}
