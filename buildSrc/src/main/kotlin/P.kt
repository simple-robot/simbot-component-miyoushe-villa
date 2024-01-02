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

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

import love.forte.gradle.common.core.project.ProjectDetail
import love.forte.gradle.common.core.project.Version
import love.forte.gradle.common.core.project.minus
import love.forte.gradle.common.core.project.version as v

val simbotVersion = v(3, 3, 0)

val simbotApi = "love.forte.simbot:simbot-api:$simbotVersion"
val simbotAnnotations = "love.forte.simbot.util:simbot-annotations:$simbotVersion"
val simbotCore = "love.forte.simbot:simbot-core:$simbotVersion"
val simbotLogger = "love.forte.simbot:simbot-logger:$simbotVersion"
val simbotLoggerSlf4j = "love.forte.simbot:simbot-logger-slf4j-impl:$simbotVersion"

val simbotRequestorCore = "love.forte.simbot.util:simbot-util-api-requestor-core:$simbotVersion"
val simbotRequestorKtor = "love.forte.simbot.util:simbot-util-api-requestor-ktor:$simbotVersion"

val simbotUtilSuspendTransformer = "love.forte.simbot.util:simbot-util-suspend-transformer:$simbotVersion"
val simbotUtilLoop = "love.forte.simbot.util:simbot-util-stage-loop:$simbotVersion"
val simbotUtilAnnotations = "love.forte.simbot.util:simbot-annotations:$simbotVersion"


object P : ProjectDetail() {
    const val GROUP = "love.forte.simbot.component"
    const val DESCRIPTION = "Simple Robot框架下针对米游社大别野的组件实现"
    const val HOMEPAGE = "https://github.com/simple-robot/simbot-component-miyoushe"

    override val homepage: String
        get() = HOMEPAGE

    private val baseVersion = v(0, 0, 2)

//    private val alphaSuffix = v("alpha", 1)
    // baseVersion - alphaSuffix

    override val version: Version = baseVersion // version(0, 0, 1)

    val snapshotVersion: Version =
        baseVersion - Version.SNAPSHOT

    override val group: String get() = GROUP
    override val description: String get() = DESCRIPTION
    override val developers: List<Developer> = developers {
        developer {
            id = "forte"
            name = "ForteScarlet"
            email = "ForteScarlet@163.com"
            url = "https://github.com/ForteScarlet"
        }
        developer {
            id = "forliy"
            name = "ForliyScarlet"
            email = "ForliyScarlet@163.com"
            url = "https://github.com/ForliyScarlet"
        }
    }

    override val licenses: List<License> = licenses {
        license {
            name = "GNU GENERAL PUBLIC LICENSE, Version 3"
            url = "https://www.gnu.org/licenses/gpl-3.0-standalone.html"
        }
        license {
            name = "GNU LESSER GENERAL PUBLIC LICENSE, Version 3"
            url = "https://www.gnu.org/licenses/lgpl-3.0-standalone.html"
        }
    }

    override val scm: Scm = scm {
        url = HOMEPAGE
        connection = "scm:git:$HOMEPAGE.git"
        developerConnection = "scm:git:ssh://git@github.com/simple-robot/simbot-component-miyoushe.git"
    }


}

private val _isSnapshot by lazy { initIsSnapshot() }

private fun initIsSnapshot(): Boolean {
    val property = System.getProperty("simbot.snapshot").toBoolean()
    val env = System.getenv(Env.IS_SNAPSHOT).toBoolean()

    return property || env
}

fun isSnapshot(): Boolean = _isSnapshot
