data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val build: Int
) {
    val name get() = "$major.$minor.$patch"
    val fullName get() = "$name ($build)"
    val code get() = major * 1000000 + minor * 10000 + patch * 100 + build
}

const val AppNamespace = "dev.luisramos.kmmstarter"

object Versions {
    private const val BuildNumber = 1
    val Build get() = System.getProperty("buildNumber")?.toInt() ?: BuildNumber

    val App = Version(0, 1, 0, Build)

    object Android {
        const val CompileSdk = 33
        const val MinSdk = 24
        const val TargetSdk = 33
    }

    const val Kotlin = "1.8.0"
    const val Ktor = "2.0.0"
    const val SqlDelight = "1.5.5"
    const val ComposeCompiler = "1.4.0"
}

object Dependencies {
    const val Datetime = "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0"
    const val Serialization =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"
    const val Settings = "com.russhwolf:multiplatform-settings-no-arg:1.0.0"
    const val Coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4"

    object Kermit {
        const val version = "1.2.2"
        const val Core = "co.touchlab:kermit:$version"
    }

    object Ktor {
        const val Core = "io.ktor:ktor-client-core:${Versions.Ktor}"
        const val OkHttp = "io.ktor:ktor-client-okhttp:${Versions.Ktor}"
        const val iOS = "io.ktor:ktor-client-ios:${Versions.Ktor}"
        const val Serialization = "io.ktor:ktor-serialization-kotlinx-json:${Versions.Ktor}"
    }

    object SqlDelight {
        const val Android = "com.squareup.sqldelight:android-driver:${Versions.SqlDelight}"
        const val iOS = "com.squareup.sqldelight:native-driver:${Versions.SqlDelight}"
        const val Jdbc = "com.squareup.sqldelight:sqlite-driver:${Versions.SqlDelight}"
        const val Coroutines =
            "com.squareup.sqldelight:coroutines-extensions:${Versions.SqlDelight}"
    }

    object Android {

        object Compose {
            const val Version = "1.3.3"

            const val Ui = "androidx.compose.ui:ui:$Version"
            const val UiTooling = "androidx.compose.ui:ui-tooling:$Version"
            const val UiToolingPreview = "androidx.compose.ui:ui-tooling-preview:$Version"
            const val Foundation = "androidx.compose.foundation:foundation:1.3.1"
            const val Material = "androidx.compose.material:material:1.3.1"
            const val Activity = "androidx.activity:activity-compose:1.6.1"
        }
    }
}