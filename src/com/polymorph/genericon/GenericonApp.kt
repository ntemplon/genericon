package com.polymorph.genericon

import javafx.application.Application
import tornadofx.*

class GenericonApp: App(TestView::class) {

}

fun main(args: Array<String>) {
    println("Hello World")
    Application.launch(GenericonApp::class.java, *args)
}