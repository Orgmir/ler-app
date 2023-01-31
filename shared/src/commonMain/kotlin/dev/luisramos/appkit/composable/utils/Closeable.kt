package dev.luisramos.appkit.composable.utils

interface Closeable {
    fun close()

    companion object {
        operator fun invoke(block: () -> Unit): Closeable = object : Closeable {
            override fun close() {
                block()
            }
        }
    }
}
