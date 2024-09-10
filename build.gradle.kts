/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://maven.pkg.github.com/APDevTeam/Movecraft")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://maven.enginehub.org/repo/")
    }

    maven {
        url = uri("https://repo.citizensnpcs.co/")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }

    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    api(libs.net.citizensnpcs.citizens.main)
    compileOnly(libs.org.spigotmc.spigot.api)
    compileOnly(libs.com.sk89q.worldguard.worldguard.bukkit)
    compileOnly(libs.com.github.milkbowl.vaultapi)
    system(libs.com.degitise.minevid.dtltraders)
}

group = "net.apdevteam.apautonpc"
version = "4.0.0_beta-2"
description = "APAutoNPC"
java.sourceCompatibility = JavaVersion.VERSION_13

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}
