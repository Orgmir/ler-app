package dev.luisramos.kmmstarter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform