plugins {
    java

    //Gradle shadow plugin to make fatjar
    id("com.github.johnrengelman.shadow") version ("7.0.0")
}

group = "com.novamaday.d4j.gradle"
version = "2021.08.31"

repositories {
    mavenCentral()
}

sourceSets {
    all {
        dependencies {
            implementation("com.discord4j:discord4j-core:3.2.0-RC3")
            implementation("ch.qos.logback:logback-classic:1.2.3")
        }
    }
}

/*
Configure the sun.tools.jar.resources.jar task for our main class and so that `./gradlew build` always makes the fatjar
This boilerplate is completely removed when using Springboot
 */
tasks.jar {
    manifest {
        attributes("Main-Class" to "com.novamaday.d4j.gradle.simplebot.SimpleBot")
    }

    finalizedBy("shadowJar")
}
