package com.github.jsocle.receptionist

abstract class Config {
    val port: Int
        get() = (System.getenv("PORT") ?: "8080").toInt()
}

class LocalConfig : Config() {
}

fun buildConfig(): LocalConfig {
    return LocalConfig()
}

val config = buildConfig()
