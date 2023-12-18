dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("kotlincrypto") {
            // see https://github.com/KotlinCrypto/version-catalog
            // https://github.com/KotlinCrypto/version-catalog/blob/master/gradle/kotlincrypto.versions.toml
            from("org.kotlincrypto:version-catalog:0.4.0")
        }
    }
}

rootProject.name = "simbot-component-miyoushe-vila"

include("simbot-component-miyoushe-vila-api")
