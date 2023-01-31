@file:Suppress("ktlint:filename")

package dev.luisramos.appkit.composable.utils

internal expect class WeakReference<T : Any>(value: T) {
    val value: T?
}
