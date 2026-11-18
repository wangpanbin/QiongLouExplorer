plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.bin.qionglouexplorer"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.bin.qionglouexplorer"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
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

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// AGP 9 已移除 compileOptions.annotationProcessorOptions，
// 注解处理器参数改走 Variant API（最终转成 javac 的 -A<key>=<value>）
androidComponents {
    onVariants { variant ->
        variant.javaCompilation?.annotationProcessor?.arguments?.put(
            "room.schemaLocation",
            "$projectDir/schemas",
        )
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.core.ktx)
    implementation(libs.recyclerview)
    implementation(libs.fragment)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)

    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    implementation(libs.gson)
    implementation(libs.zxing.android.embedded)

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
