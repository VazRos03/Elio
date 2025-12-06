// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Mantener los aliases (si usas un archivo libs.versions.toml)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false


    // asegurando que está definido ANTES de que se apliquen los plugins de las aplicaciones.
    id("com.google.gms.google-services") version "4.4.0" apply false
}