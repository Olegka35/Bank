plugins {
    id("java")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-config-server")
}

tasks.test {
    useJUnitPlatform()
}