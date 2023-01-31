import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    //trick: for the same plugin versions in all sub-modules
    //Don't use Versions object so Android Studio updates this automatically
    id("com.android.application").version("7.4.0").apply(false)
    id("com.android.library").version("7.4.0").apply(false)
    kotlin("android").version(Versions.Kotlin).apply(false)
    kotlin("multiplatform").version(Versions.Kotlin).apply(false)
    kotlin("plugin.serialization").version(Versions.Kotlin).apply(false)
    id("app.cash.sqldelight").version(Versions.SqlDelight).apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}