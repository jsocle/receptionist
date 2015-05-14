package com.github.jsocle.receptionist

import com.github.jsocle.JSocle

public object app : JSocle() {
    init {
        route("/") { ->
            return@route "receptionist"
        }
    }
}


fun main(args: Array<String>) {
    app.run(config.port)
}
