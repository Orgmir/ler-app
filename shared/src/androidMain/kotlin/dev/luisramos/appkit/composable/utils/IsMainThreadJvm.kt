package dev.luisramos.appkit.composable.utils

import android.os.Looper

internal actual fun isMainThread(): Boolean =
    Looper.getMainLooper().thread == Thread.currentThread()
