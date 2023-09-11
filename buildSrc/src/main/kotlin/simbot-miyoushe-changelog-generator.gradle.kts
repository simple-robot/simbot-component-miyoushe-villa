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

import changelog.generateChangelog


tasks.create("createChangelog") {
    group = "documentation"
    doFirst {
        generateChangelog("v${P.version}")
    }
}

tasks.create("updateWebsiteVersionJson") {
    group = "documentation"
    doFirst {
        val version = P.version.toString()

        val websiteVersionJsonDir = rootProject.file("website/static")
        if (!websiteVersionJsonDir.exists()) {
            websiteVersionJsonDir.mkdirs()
        }
        val websiteVersionJsonFile = File(websiteVersionJsonDir, "version.json")
        if (!websiteVersionJsonFile.exists()) {
            websiteVersionJsonFile.createNewFile()
        }

        websiteVersionJsonFile.writeText(
            """
            {
              "version": "$version"
            }
        """.trimIndent()
        )
    }
}

//fun repoRow(moduleName: String, group: String, id: String, version: String): String {
//    return "| $moduleName | [$moduleName: v$version](https://repo1.maven.org/maven2/${group.replace(".", "/")}/${id.replace(".", "/")}/$version) | [$moduleName: v$version](https://search.maven.org/artifact/$group/$id/$version/jar)  |"
//}
