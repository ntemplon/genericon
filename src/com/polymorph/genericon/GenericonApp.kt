package com.polymorph.genericon

import com.polymorph.genericon.ui.TestView
import javafx.application.Application
import tornadofx.*

class GenericonApp: App(TestView::class) {

}

fun main(args: Array<String>) {
    Application.launch(GenericonApp::class.java, *args)
}