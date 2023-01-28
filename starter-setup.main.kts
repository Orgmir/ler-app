#!/usr/bin/env kotlin

import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.pathString
import kotlin.io.path.readText
import kotlin.system.exitProcess

println("Setting up starter project...")
println("Root project name:")
val projectName = readLine().orEmpty()
if (projectName.isBlank()) {
    println("Please provide a project name.")
    exitProcess(1)
}

println("App package/bundle id (com.example.app):")
val packageName = readLine().orEmpty()
if (packageName.isBlank()) {
    println("Please provide a valid package.")
    exitProcess(1)
}
val folders = packageName.replace(".", "/")

println("Setting rootProject.name in settings.gradle.kts to $projectName")
replaceInFile(
    "settings.gradle.kts",
    Regex("(?<=rootProject.name = \")(.*)(?=\")"), projectName
)

println("Setting AppNamespace in Dependencies.kt to $packageName")
replaceInFile(
    "buildSrc/src/main/kotlin/Dependencies.kt",
    Regex("(?<=const val AppNamespace = \")(.*)(?=\")"), packageName
)

println("Setting Bundle identifier in iOS .xcodeproj to $packageName")
replaceInFile(
    "iosApp/iosApp.xcodeproj/project.pbxproj",
    "(?<=PRODUCT_BUNDLE_IDENTIFIER = )(.*)(?=;)".toRegex(),
    packageName
)

listOf(
    "shared/src/androidMain/kotlin/",
    "shared/src/commonMain/kotlin/",
    "shared/src/iosMain/kotlin/",
    "androidApp/src/main/java/",
).forEach { pathname ->
    // find all files in folder
    val path = Path.of(pathname)
    Files.walk(path)
        .filter(Files::isRegularFile)
        .forEach { file ->
            val text = file.readText()
            // get old package
            val oldPackage = "(?<=^package\\s)(.*)".toRegex().find(text)?.value.orEmpty()
            // replace with new package
            if (oldPackage.isNotEmpty()) {
                println("${file.pathString}: Renaming package")
                val newText = text.replace(oldPackage, packageName)

                // create new folder structure
                Path.of("$pathname$folders").toFile().mkdirs()

                // move file to new folder structure
                val newFile = Path.of("$pathname$folders", file.name).toFile()
                println("${file.pathString}: Moving to ${newFile.absolutePath}")
                newFile.createNewFile()
                newFile.appendText(newText)

                // delete existing file
                file.deleteIfExists()
            }
        }

    // Delete empty directories
    deleteIfEmpty(path)
}

fun replaceInFile(filename: String, regex: Regex, newValue: String) {
    val file = File(filename)
    val newFile = file.readText()
        .replace(regex, newValue)
    FileWriter(file).use { it.write(newFile) }
}

fun deleteIfEmpty(dir: Path) {
    // ignore if file
    if (!dir.isDirectory()) return
    // Try to delete children
    Files.list(dir).forEach {
        deleteIfEmpty(it)
    }
    // delete self if empty
    if (!Files.list(dir).findAny().isPresent) {
        Files.delete(dir)
    }
}