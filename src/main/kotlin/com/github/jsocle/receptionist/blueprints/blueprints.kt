package com.github.jsocle.receptionist.blueprints

import com.github.jsocle.Blueprint
import com.github.jsocle.receptionist.layout

object mainApp : Blueprint() {
    val index = route("/") { -> layout {} }
}