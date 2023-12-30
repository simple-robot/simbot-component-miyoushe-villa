plugins {
    `simbot-miyoushe-nexus-publish`
    `simbot-miyoushe-changelog-generator`
    `simbot-miyoushe-dokka-multi-module`
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
