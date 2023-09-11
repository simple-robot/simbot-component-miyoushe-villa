/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-miyoushe.
 *
 * simbot-component-miyoushe is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-miyoushe is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-miyoushe,
 * If not, see <https://www.gnu.org/licenses/>.
 */

import love.forte.gradle.common.core.Gpg
import love.forte.gradle.common.publication.configure.jvmConfigPublishing
import util.checkPublishConfigurable
import util.isCi
import util.isLinux

plugins {
    id("signing")
    id("maven-publish")
}

val (isSnapshotOnly, isReleaseOnly, isPublishConfigurable) = checkPublishConfigurable()


logger.info("isSnapshotOnly: $isSnapshotOnly")
logger.info("isReleaseOnly: $isReleaseOnly")
logger.info("isPublishConfigurable: $isPublishConfigurable")

if (!isCi || isLinux) {
    checkPublishConfigurable {
        jvmConfigPublishing {
            project = P
            publicationName = "kookDist"
            val jarSources by tasks.registering(Jar::class) {
                archiveClassifier.set("sources")
                from(sourceSets["main"].allSource)
            }

            val jarJavadoc by tasks.registering(Jar::class) {
                archiveClassifier.set("javadoc")
            }

            artifact(jarSources)
            artifact(jarJavadoc)

            isSnapshot = isSnapshot()
            releasesRepository = ReleaseRepository
            snapshotRepository = SnapshotRepository
            gpg = if (isSnapshot()) null else Gpg.ofSystemPropOrNull()
        }

        if (isSnapshot()) {
            publishing {
                publications.withType<MavenPublication> {
                    version = P.snapshotVersion.toString()
                }
            }
        }

        publishing {
            publications.withType<MavenPublication> {
                show()
            }
        }


    }
}


fun MavenPublication.show() {
    //// show project info
    println("========================================================")
    println("== MavenPublication for ${project.name}")
    println("== maven.pub.group:       $group")
    println("== maven.pub.name:        $name")
    println("== project.verson:        ${project.version}")
    println("== maven.pub.version:     $version")
    println("== maven.pub.description: $description")
    println("========================================================")
}


inline val Project.sourceSets: SourceSetContainer
    get() = extensions.getByName("sourceSets") as SourceSetContainer
