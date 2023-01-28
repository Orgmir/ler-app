plugins {
    id("com.android.application")
    kotlin("android")
}

//val keystoreProperties = java.util.Properties()
//val keystorePropertiesFile = rootProject.file("keystore.properties")
//if (!keystorePropertiesFile.exists()) {
//    throw GradleException(
//        "File keystore.properties is missing in the root of the project. " +
//                "It contains the keystore's passwords to sign release builds."
//    )
//}
//keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = AppNamespace
    compileSdk = Versions.Android.CompileSdk
    defaultConfig {
        applicationId = AppNamespace
        minSdk = Versions.Android.MinSdk
        targetSdk = Versions.Android.TargetSdk
        versionCode = Versions.App.code
        versionName = Versions.App.name
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.ComposeCompiler
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            // Remove debug options for coroutines
            // https://github.com/Kotlin/kotlinx.coroutines/tree/master#avoiding-including-the-debug-infrastructure-in-the-resulting-apk
            excludes += "DebugProbesKt.bin"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    with(Dependencies.Android.Compose) {
        implementation(Ui)
        implementation(UiTooling)
        implementation(UiToolingPreview)
        implementation(Foundation)
        implementation(Material)
        implementation(Activity)
    }
}