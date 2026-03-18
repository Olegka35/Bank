plugins {
    id("java")
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-tracing-bridge-brave") // Трейсер Brave с мостом к нему
    implementation("io.zipkin.reporter2:zipkin-reporter-brave") // Brave-репортер используется для отправки трасс
}

tasks.test {
    useJUnitPlatform()
}