import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Kotlin.version
    id("com.github.johnrengelman.shadow") version "2.0.2"
    id("com.bmuschko.tomcat") version "2.5"
    application
    war
}

group = "moe.nikky"
version = "0.1-SNAPSHOT"

application {
//    mainClassName = "io.ktor.server.netty.DevelopmentEngine"
    mainClassName = "io.ktor.server.netty.DevelopmentEngine"
}

war {
    webAppDirName = "webapp"
}

tomcat {
    contextPath = "/"
    httpProtocol = "org.apache.coyote.http11.Http11Nio2Protocol"
    ajpProtocol = "org.apache.coyote.ajp.AjpNio2Protocol"
}

//tomcat {
//}

repositories {
    maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    maven(url = "http://dl.bintray.com/kotlin/ktor")
    maven(url = "https://dl.bintray.com/kotlin/kotlinx")
    mavenCentral()
    jcenter()
//    maven(url = "https://dl.bintray.com/pgutkowski/Maven")
    maven(url = "https://dl.bintray.com/kotlin/squash")
    maven(url = "https://jitpack.io")
}

dependencies {
    compile(kotlin("stdlib-jdk8", Kotlin.version))
    compile(kotlin("reflect", Kotlin.version))
//    compile(kotlin("runtime", Kotlin.version))

    compile(ktor())
    compile(ktor("jackson"))
    compile(ktor("features"))
    compile(ktor("locations"))
    compile(ktor("html-builder"))
    compile(ktor("server-core"))
    compile(ktor("server-netty"))
    compile(ktor("server-servlet"))

    tomcat("org.apache.tomcat.embed:tomcat-embed-core:${Tomcat.version}",
            "org.apache.tomcat.embed:tomcat-embed-jasper:${Tomcat.version}")

    // Logging
    compile(group = "ch.qos.logback", name = "logback-classic", version = Logback.version)

    // Networking
    compile(group = "com.github.kittinunf.fuel", name = "fuel", version = Fuel.version)
    compile(group = "com.github.kittinunf.fuel", name = "fuel-coroutines", version = Fuel.version)

    // GraphQL
    compile(group = "com.github.NikkyAi", name = "KGraphQL", version = "0.3.0-ALPHA-SNAPSHOT")
//    compile("com.github.pgutkowski:kgraphql:${KGraphQL.version}")

    // Dependency Injection
    compile(group = "org.koin", name = "koin-ktor", version = Koin.version)

    // Database
    compile(group = "org.jetbrains.squash", name = "squash-h2", version = Squash.version)

    compile(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.9.5")
    compile(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = "2.9.5")

    // Testing
    testCompile(group = "junit", name = "junit", version = "4.12")
    testCompile(ktor("server-test-host"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        languageVersion = "1.3"
        jvmTarget = "1.8"
    }
}

kotlin {
//    experimental.coroutines = Coroutines.ENABLE
}

tasks.withType<Wrapper> {
    gradleVersion = Gradle.version
    distributionType = Gradle.distributionType // Wrapper.DistributionType.ALL
}