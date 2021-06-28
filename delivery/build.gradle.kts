val newrelicVersion = "5.7.0"
val jacksonVersion = "2.12.2"
val micrometerVersion = "1.5.1"
val swaggerVersion = "2.9.2"

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-noarg:1.3.71")
    }
}

plugins {
    id("org.springframework.boot") version "2.3.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("plugin.spring") version "1.3.72"
    application
    kotlin("jvm")
    kotlin("plugin.jpa") version "1.2.71"
}

group = "com.fiuba"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()

}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation(kotlin("stdlib-jdk8"))

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.skyscreamer:jsonassert:1.5.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    mainClassName = "com.fiuba.rentapp.delivery.RentappApplication"
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
