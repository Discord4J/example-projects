plugins {
    java

    //Spring plugin
    id("org.springframework.boot") version ("2.5.4")
}

group = "com.novamaday.d4j.gradle"
version = "2021.08.31"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.discord4j:discord4j-core:3.2.0")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.4")
}
