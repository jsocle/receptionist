package com.github.jsocle.receptionist

import com.github.jsocle.JSocle
import com.github.jsocle.receptionist.blueprints.mainBlueprint

object app : JSocle() {
    init {
        app.register(mainBlueprint)
    }
}


fun main(args: Array<String>) {
    app.run(config.port)
}
