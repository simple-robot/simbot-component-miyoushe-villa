import love.forte.gradle.common.core.project.setup

plugins {
    kotlin("jvm")
//    `miyoushe-multiplatform-maven-publish`
    kotlin("plugin.serialization")
//    `miyoushe-dokka-partial-configure`
}

setup(P)
if (isSnapshot()) {
    version = P.snapshotVersion.toString()
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        javaParameters = true
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + listOf("-Xjvm-default=all")
    }
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    sourceSets.configureEach {
        languageSettings {
            optIn("kotlin.RequiresOptIn")
        }
    }

    dependencies {
        compileOnly(simbotAnnotations)
        api(project(":simbot-component-miyoushe-villa-api"))
        api(project(":simbot-component-miyoushe-villa-stdlib"))
        api(simbotApi) // use @Api4J annotation
        api(simbotUtilSuspendTransformer)
        api(simbotUtilLoop)
        api(simbotLogger)

        api(libs.kotlinx.coroutines.core)
        api(libs.ktor.client.ws)
        api(libs.ktor.client.core)
        api(libs.ktor.client.contentNegotiation)
        api(libs.ktor.serialization.kotlinx.json)
        api(libs.kotlinx.serialization.json)
        api(libs.kotlinx.serialization.protobuf)
        // default client engine: CIO
        api(libs.ktor.client.cio)
        // for multiplatform md5
//        api(kotlincrypto.hash.md5)

        testImplementation(kotlin("test-junit5"))
        testImplementation(libs.kotlinx.coroutines.test)
        testImplementation(libs.ktor.client.logging)
        testImplementation(libs.okio)

        testImplementation(simbotApi) // use @Api4J annotation
        testImplementation(simbotLoggerSlf4j)
    }

}

// suppress all?
//tasks.withType<org.jetbrains.dokka.gradle.DokkaTaskPartial>().configureEach {
//    dokkaSourceSets.configureEach {
//        suppress.set(true)
//        perPackageOption {
//            suppress.set(true)
//        }
//    }
//}

