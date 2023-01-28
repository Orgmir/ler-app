package app.luisramos.ler

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform