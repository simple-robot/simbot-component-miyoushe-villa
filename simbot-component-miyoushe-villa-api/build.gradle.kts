import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.kotlin.multiplatform.applyTier1
import love.forte.gradle.common.kotlin.multiplatform.applyTier2
import love.forte.gradle.common.kotlin.multiplatform.applyTier3

plugins {
    kotlin("multiplatform")
    `miyoushe-multiplatform-maven-publish`
    kotlin("plugin.serialization")
    `miyoushe-dokka-partial-configure`
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

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()

    sourceSets.configureEach {
        languageSettings {
//            optIn("love.forte.simbot.qguild.InternalApi")
            optIn("kotlin.js.ExperimentalJsExport")
        }
    }

    jvm {
        withJava()
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                javaParameters = true
                freeCompilerArgs = freeCompilerArgs + listOf("-Xjvm-default=all")
            }
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        useEsModules()
        nodejs()
        generateTypeScriptDefinitions()
        binaries.library()
        compilations.all {
            // Enables ES6 classes generation
            kotlinOptions {
                suppressWarnings
                freeCompilerArgs = freeCompilerArgs + listOf("-Xsuppress:NON_EXPORTABLE_TYPE")
                useEsClasses = true
            }
        }
    }

    // see https://kotlinlang.org/docs/native-target-support.html
    applyTier1(supportKtorClient = true, supportKtorServer = true)
    applyTier2(supportKtorClient = true, supportKtorServer = true, linuxArm64 = true)
    applyTier3(supportKtorClient = true, supportKtorServer = true)

    sourceSets {
        commonMain {
            dependencies {
                compileOnly(simbotAnnotations)
//                api(simbotRequestorCore)
                api(simbotUtilSuspendTransformer)
                api(libs.kotlinx.coroutines.core)
                api(libs.ktor.client.core)
                api(libs.ktor.client.contentNegotiation)
                api(libs.ktor.serialization.kotlinx.json)
                api(libs.kotlinx.serialization.json)
                api(libs.kotlinx.serialization.protobuf)
                api(simbotLogger)
                // for multiplatform md5
                api(kotlincrypto.hash.md5)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.ktor.client.ws)
                implementation(libs.ktor.client.logging)
                implementation(libs.okio)
            }
        }

        jvmMain {
            dependencies {
                compileOnly(simbotApi) // use @Api4J annotation
                compileOnly(simbotAnnotations) // use @Api4J annotation
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.server.cio)
                implementation(simbotApi) // use @Api4J annotation
                implementation(simbotLoggerSlf4j)
//                // use @Api4J annotation
//                implementation(libs.log4j.api)
//                implementation(libs.log4j.core)
//                implementation(libs.log4j.slf4j2Impl)
            }
        }

        jsMain {
            dependencies {
                api(simbotAnnotations)
                api(libs.ktor.client.js)
            }
        }
        jsTest {
            dependencies {
                api(libs.ktor.client.js)
                api(libs.okio.nodefs)
            }
        }

        mingwTest {
            dependencies {
                implementation(libs.ktor.client.winhttp)
            }
        }

        linuxTest {
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }

        appleTest {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
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

