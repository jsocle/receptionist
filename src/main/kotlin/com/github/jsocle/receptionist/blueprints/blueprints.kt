package com.github.jsocle.receptionist.blueprints

import com.github.jsocle.Blueprint

object mainApp : Blueprint() {
    val index = route("/") { -> }
}