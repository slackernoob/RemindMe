plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    id("kotlin-kapt")
    alias(libs.plugins.ksp) // Kotlin Symbol Processing (for annotation processors like Room/Hilt)
//    jacoco
    id("jacoco")
    id("app.cash.sqldelight") version "2.1.0"
}
jacoco {
    toolVersion = "0.8.13"
}

sqldelight {
    databases {
        create("TaskDatabase") {
            packageName.set("com.example.remindme.data")
        }
    }
}

android {
    namespace = "com.example.remindme"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.remindme"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.remindme.HiltTestRunner"
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md"
            )
        }
    }
}

dependencies {
    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.navigation.compose)

    // Lifecycle + ViewModel
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Kotlin Coroutines + Flow
    implementation(libs.kotlinx.coroutines.android)

    // Room database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    implementation("app.cash.sqldelight:android-driver:2.1.0") // or latest
    implementation("app.cash.sqldelight:coroutines-extensions:2.1.0")


    // Hilt Dependency Injection
    implementation(libs.dagger.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Core Android KTX
    implementation(libs.androidx.core.ktx)

    // Testing - Unit Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation("android.arch.core:core-testing:1.0.0")
    testImplementation("app.cash.sqldelight:sqlite-driver:2.1.0")

    // Testing - Instrumented/Android Tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.56.2")

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(kotlin("test"))
}
// To generate JaCoCo report
tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest", "connectedDebugAndroidTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/NavGraph.kt",
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/*\$*Companion*",
        "**/*$*Composable*",
        "**/*\$*Lambda*",
        "**/*\$*Impl*",
        "**/*Hilt*.*",
        "**/*Dagger*.*",
        "**/*_Factory*.*",
        "**/generated/**",
        "**/hilt_aggregated_deps/**",
        "**/dagger/hilt/internal/aggregatedroot/codegen/**",
        "**/di/**",
        "**/ui/theme/**", // exclude theme coverage
        "**/com/example/remindme/data/*_Impl*.*", // Room generated classes
        "**/com/example/remindme/data/TaskDatabase_Impl.*"
    )
//    val debugTree = fileTree("${layout.buildDirectory.get().asFile}/intermediates/javac/debug/compileDebugJavaWithJavac/classes") {
//        exclude(fileFilter)
//    }

    val kotlinDebugTree = fileTree("${layout.buildDirectory.get().asFile}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }

//    val mainSrc = "$projectDir/src/main/java"
    val mainSrc = project.extensions
        .getByType<com.android.build.gradle.AppExtension>()
        .sourceSets
        .getByName("main")
        .java.srcDirs

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files( kotlinDebugTree))
    executionData.setFrom(
        fileTree(layout.buildDirectory.get().asFile) {
            include(
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                "outputs/code_coverage/debugAndroidTest/connected/**/coverage.ec"
            )
        }
    )
}