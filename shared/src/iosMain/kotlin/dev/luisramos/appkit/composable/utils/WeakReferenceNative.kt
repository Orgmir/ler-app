@file:Suppress("ktlint:filename")

package dev.luisramos.appkit.composable.utils

import kotlin.native.ref.WeakReference

internal actual class WeakReference<T : Any> actual constructor(value: T) {
    private val weak = WeakReference(value)
    actual val value: T? get() = weak.value
}
