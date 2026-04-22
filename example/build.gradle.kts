plugins {
    java
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

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
