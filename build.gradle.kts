plugins {
    kotlin("jvm") version "2.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.0")
    compileOnly("cz.jirutka.rsql:rsql-parser:2.1.0")

    testImplementation(kotlin("test"))
    testImplementation("cz.jirutka.rsql:rsql-parser:2.1.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(19)
}