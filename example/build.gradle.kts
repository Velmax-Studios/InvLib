plugins {
    java
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

group = "me.velmax.invlib.example"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.3-R0.1-SNAPSHOT")
    implementation(project(":"))
}

tasks {
    shadowJar {
        // Relocate the library but NOT the example plugin itself
        relocate("me.velmax.invlib", "me.velmax.invlib.example.shaded.invlib") {
            exclude("me.velmax.invlib.example.**")
        }
        
        archiveFileName.set("InvLibExample-${project.version}.jar")
    }

    build {
        dependsOn(shadowJar)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
