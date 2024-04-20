// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        val navversion = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navversion")
        classpath("com.google.gms:google-services:4.4.1")
    }
}