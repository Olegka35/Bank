plugins {
	id("java")
	id("org.springframework.boot") version "4.0.1" apply false
	id("io.spring.dependency-management") version "1.1.7"
}

allprojects {
	group = "com.tarasov.bank"
	version = "1.0.0"

	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "io.spring.dependency-management")

	dependencyManagement {
		imports {
			mavenBom("org.springframework.boot:spring-boot-dependencies:4.0.1")
			mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.1.0")
		}
	}

	dependencies {
		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")

		testImplementation(platform("org.junit:junit-bom:5.10.0"))
		testImplementation("org.junit.jupiter:junit-jupiter")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	}
}