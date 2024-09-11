plugins {
    `java-library`
    `maven-publish`
    id("io.github.0ffz.github-packages") version "1.2.1"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven { githubPackage("apdevteam/movecraft")(this) }
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.citizensnpcs.co/")
    maven("https://jitpack.io")
}

dependencies {
    api("org.jetbrains:annotations-java5:24.1.0")
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("net.countercraft:movecraft:+")
    compileOnly("net.citizensnpcs:citizensapi:+")
    compileOnly(files("libs/dtlTraders.jar"))
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    api("com.sk89q.worldguard:worldguard-bukkit:7.0.10")
}

group = "net.apdevteam.apautonpc"
version = "4.0.0_beta-3"
description = "APAutoNPC"
java.toolchain.languageVersion = JavaLanguageVersion.of(21)

tasks.jar {
    archiveBaseName.set("APAutoNPC")
    archiveClassifier.set("")
    archiveVersion.set("")
}

tasks.processResources {
    from(rootProject.file("LICENSE.md"))
    filesMatching("*.yml") {
        expand(mapOf("projectVersion" to project.version))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.apdevteam.apautonpc"
            artifactId = "apautonpc"
            version = "${project.version}"

            artifact(tasks.jar)
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/apdevteam/apautonpc")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
