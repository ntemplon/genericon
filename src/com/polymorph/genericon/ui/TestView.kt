package com.polymorph.genericon.ui

import com.polymorph.genericon.math.NumericCategory
import tornadofx.*

class TestView : View("Test View") {
    private val model: NumericCategoryModel by inject()

    override val root = borderpane {
        top {
            menubar {
                menu("File") {
                    item("Exit") {
                        action {
                            currentStage?.close()
                        }
                    }
                }

                menu("Edit") {
                    item("Numeric Types") {
                        action {
                            // TODO - show popup
                        }
                    }
                }
            }
        }

        center {
            borderpane {
                center {
                    val gm = NumericCategory.gameMechanic
                    val ut = NumericCategory.untyped

                    tableview(NumericCategory.categories.observable()) {
                        column("Name", NumericCategory::name)
                        column("Stacks", NumericCategory::stacks)
                        column("Hardcoded", NumericCategory::immutable)
                        column("ID", NumericCategory::id)

                        bindSelected(model)
                    }
                }

                right {
                    add(find(NumericCategoryEditFragment::class))
                }
            }
        }
    }
}
