plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

val kotlinVersion = "1.9.21"
val dokkaVersion = "1.9.10"
val suspendTransformVersion = "0.6.0-beta3"
val gradleCommon = "0.2.0"

dependencies {
    // kotlin("jvm") apply false
    implementation(kotlin("gradle-plugin", kotlinVersion))
    implementation(kotlin("serialization", kotlinVersion))
    implementation("org.jetbrains.dokka", "dokka-gradle-plugin", dokkaVersion)
    implementation("org.jetbrains.dokka", "dokka-base", dokkaVersion)
 
    // see https://github.com/gradle-nexus/publish-plugin
    implementation("io.github.gradle-nexus:publish-plugin:1.1.0")
    
    implementation("love.forte.plugin.suspend-transform:suspend-transform-plugin-gradle:$suspendTransformVersion")
    implementation("love.forte.gradle.common:gradle-common-core:$gradleCommon")
    implementation("love.forte.gradle.common:gradle-common-kotlin-multiplatform:$gradleCommon")
    implementation("love.forte.gradle.common:gradle-common-publication:$gradleCommon")
//    implementation("love.forte.gradle.common:gradle-common-all:$gradleCommon")
}

//val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
//
//compileKotlin.kotlinOptions {
//    freeCompilerArgs += listOf("-Xinline-classes")
//}
