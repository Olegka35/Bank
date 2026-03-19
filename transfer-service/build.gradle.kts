plugins {
    id("java")
    id("org.springframework.boot")
    id("org.springframework.cloud.contract") version "5.0.1"
    id("maven-publish")
}

dependencies {
    implementation(project(":service-common"))

    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    implementation("org.springframework.boot:spring-boot-starter-aspectj")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")

    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
    testImplementation("com.fasterxml.jackson.core:jackson-core")
    testImplementation("com.fasterxml.jackson.core:jackson-databind")
    testImplementation("io.rest-assured:spring-mock-mvc:6.0.0")
    implementation("org.springframework.boot:spring-boot-starter-kafka")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-zipkin")
    implementation("io.micrometer:micrometer-registry-prometheus")
}

contracts {
    baseClassForTests.set(
        "com.tarasov.bank.transfer.contract.BaseTransferContractTest"
    )
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks.named("verifierStubsJar"))
        }
    }
}


tasks.test {
    useJUnitPlatform()
}