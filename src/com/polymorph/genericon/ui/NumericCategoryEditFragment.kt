package com.polymorph.genericon.ui

import com.polymorph.genericon.math.NumericCategory
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.Priority
import tornadofx.*

class NumericCategoryEditFragment : Fragment() {

    private val model: NumericCategoryModel by inject()


    override val root = borderpane {
        padding = Insets(5.0)
        center {
            gridpane {
                // Do nothing for column 1
                columnConstraints.add(ColumnConstraints())
                // Tell column 2 to always expand
                columnConstraints.add(ColumnConstraints().apply { hgrow = Priority.ALWAYS })

                label("Name:") {
                    gridpaneConstraints {
                        columnRowIndex(0, 0)
                        marginBottom = 5.0
                    }
                }

                textfield(model.name) {
                    enableWhen { model.mutable }

                    useMaxWidth = true
                    gridpaneConstraints {
                        columnRowIndex(1, 0)
                        marginBottom = 5.0
                    }
                }

                label("Stacks:") {
                    gridpaneConstraints {
                        columnRowIndex(0, 1)
                        marginBottom = 5.0
                    }
                }


                checkbox(null, model.stacks) {
                    enableWhen { model.mutable }

                    useMaxWidth = true
                    gridpaneConstraints {
                        columnRowIndex(1, 1)
                        marginBottom = 5.0
                    }
                }

                label("ID:") {
                    gridpaneConstraints {
                        columnRowIndex(0, 2)
                        marginBottom = 5.0
                    }
                }

                textfield(model.id) {
                    enableWhen { false.toProperty() }

                    gridpaneConstraints {
                        columnRowIndex(1, 2)
                        useMaxWidth = true
                        marginBottom = 5.0
                    }
                }
            }

            bottom {
                hbox {
                    alignment = Pos.CENTER_RIGHT
                    spacing = 5.0

                    button("OK") {
                        enableWhen { model.mutable }
                        action { model.commit() }
                    }

                    button("Cancel") {
                        action {
                            currentStage?.close()
                        }
                    }
                }
            }
        }
    }
}


class NumericCategoryModel : ItemViewModel<NumericCategory>() {
    init {
        this.item = NumericCategory.categoryLookup.values.first()
    }

    val name = bind { item.observable(NumericCategory::name) }
    val stacks = bind { item.observable(NumericCategory::stacks) }
    val id = bind { item.id.toString().toProperty() }
    val immutable = bind { item.immutable.toProperty() }
    val mutable = bind { (item.mutable).toProperty() }
}