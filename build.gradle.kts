// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
// In build.gradle.kts (Project level)
extra["compose_version"] = "1.6.8"
extra["koin_version"] = "3.5.3"