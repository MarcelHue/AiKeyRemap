import java.util.Properties

plugins {
    id("com.android.application")
}

// The release key must stay the same for every version, otherwise Obtainium cannot install updates.
// Its location is set in ~/.gradle/gradle.properties so that no secret ends up in the repository.
val signingFilePath = providers.gradleProperty("aikeyremap.signingFile").orNull
val signingProperties = Properties()
if (signingFilePath != null) {
    file(signingFilePath).inputStream().use { signingProperties.load(it) }
}

android {
    namespace = "io.github.marcelhue.aikeyremap"
    compileSdk = 37

    defaultConfig {
        applicationId = "io.github.marcelhue.aikeyremap"
        minSdk = 33
        targetSdk = 37
        versionCode = 2
        versionName = "0.2.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    signingConfigs {
        create("release") {
            if (!signingProperties.isEmpty) {
                storeFile = file(signingProperties.getProperty("storeFile"))
                storePassword = signingProperties.getProperty("storePassword")
                keyAlias = signingProperties.getProperty("keyAlias")
                keyPassword = signingProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
