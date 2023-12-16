plugins {
    `simbot-miyoushe-nexus-publish`
    `simbot-miyoushe-changelog-generator`
    `simbot-miyoushe-dokka-multi-module`
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:${libs.versions.atomicfu.get()}")
    }
}

allprojects {
    repositories {
        mavenCentral()
        love.forte.gradle.common.core.repository.Repositories.Snapshot.Default.apply {
            configMaven {
                mavenContent {
                    snapshotsOnly()
                }
            }
        }
    }
}
