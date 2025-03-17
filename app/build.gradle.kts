import java.util.Properties
import java.io.FileInputStream

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
}

android {
  namespace = "br.com.doafacil"
  compileSdk = 35

  defaultConfig {
    applicationId = "br.com.doafacil"
    minSdk = 30
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")

    if (localPropertiesFile.exists()) {
      properties.load(FileInputStream(localPropertiesFile))
      buildConfigField("String", "STRIPE_PUBLIC_KEY", "\"${properties["STRIPE_PUBLIC_KEY"]}\"")
      buildConfigField("String", "STRIPE_SECRET_KEY", "\"${properties["STRIPE_SECRET_KEY"]}\"")
    } else {
      throw GradleException("⚠️ Arquivo local.properties não encontrado! Crie-o na raiz do projeto.")
    }

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    buildConfig = true
  }

  packaging {
    resources {
      excludes += "META-INF/DEPENDENCIES"
    }
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  implementation(libs.androidx.material.icons.core.android)
  testImplementation(libs.junit)
  testImplementation("org.mockito:mockito-core:5.3.1")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  androidTestImplementation("androidx.test:core:1.5.0")
  androidTestImplementation("androidx.test:runner:1.5.2")
  androidTestImplementation("androidx.test:rules:1.5.0")
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  implementation(libs.androidx.navigation.compose)

  implementation("com.google.android.gms:play-services-auth:21.3.0")
  implementation("com.google.apis:google-api-services-calendar:v3-rev20230602-2.0.0")
  implementation("com.google.http-client:google-http-client-gson:1.46.3")

  implementation("com.stripe:stripe-android:21.6.0")

  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
}