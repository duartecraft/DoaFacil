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
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    // Variáveis do local.properties
    val localPropertiesFile = rootProject.file("local.properties")
    val properties = mutableMapOf<String, String>()

    if (localPropertiesFile.exists()) {
      localPropertiesFile.forEachLine { line ->
        val parts = line.split("=")
        if (parts.size == 2) {
          val key = parts[0].trim()
          val value = parts[1].trim()
          properties[key] = value
        }
      }
    }

    val googleClientId: String? = properties["GOOGLE_CLIENT_ID"]
    val googleClientSecret: String? = properties["GOOGLE_CLIENT_SECRET"]
    val googleRefreshToken: String? = properties["GOOGLE_REFRESH_TOKEN"]
    
    googleClientId?.let {
      buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$it\"")
    } ?: println("⚠ GOOGLE_CLIENT_ID não encontrado no local.properties!")

    googleClientSecret?.let {
      buildConfigField("String", "GOOGLE_CLIENT_SECRET", "\"$it\"")
    } ?: println("⚠ GOOGLE_CLIENT_SECRET não encontrado no local.properties!")

    googleRefreshToken?.let {
      buildConfigField("String", "GOOGLE_REFRESH_TOKEN", "\"$it\"")
    } ?: println("⚠ GOOGLE_REFRESH_TOKEN não encontrado no local.properties!")
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
      excludes += "META-INF/INDEX.LIST"
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
  implementation("androidx.compose.material3:material3:1.2.0")

  implementation(libs.androidx.navigation.compose)

  // Dependências para o Calendário Interativo
  implementation("com.maxkeppeler.sheets-compose-dialogs:core:1.0.2")
  implementation("com.maxkeppeler.sheets-compose-dialogs:calendar:1.0.2")

  // Google Play Services Auth para login no Google (Autenticação)
  implementation("com.google.android.gms:play-services-auth:20.7.0")

  // Google Calendar API
  implementation("com.google.api-client:google-api-client-android:1.33.2")
  implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
  implementation("com.google.auth:google-auth-library-oauth2-http:1.30.0")
  implementation("com.google.apis:google-api-services-calendar:v3-rev20250115-2.0.0")
}