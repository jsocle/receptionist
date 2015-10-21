package com.github.jsocle.receptionist

import com.github.jsocle.JSocleConfig

abstract class Config : JSocleConfig(secretKey = "Very Secret Key ;)".toByteArray()) {
    val port: Int
        get() = (System.getenv("PORT") ?: "8080").toInt()
}

class LocalConfig : Config() {
}

fun buildConfig(): Config {
    return LocalConfig()
}

val config = buildConfig()
