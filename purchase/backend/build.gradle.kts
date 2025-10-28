plugins {
	java
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.example"
version = "1.0.0"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// ============================================
	// Spring Boot Starters
	// ============================================
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// ============================================
	// H2 Database (In-Memory Database)
	// ============================================
	implementation("com.h2database:h2")

	// ============================================
	// Lombok (Optional - Reduces Boilerplate Code)
	// ============================================
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// OpenAPI/Swagger Documentation

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")


	// Development Tools

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// Testing

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// ============================================
// Additional Configuration
// ============================================

// Ensure UTF-8 encoding
tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}

// Custom JAR name
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	archiveFileName.set("purchase-management-${project.version}.jar")
}

//// Print startup message
//tasks.register("info") {
//	doLast {
//		println("""
//            ════════════════════════════════════════════════════════════
//                    Purchase Management System - Build Info
//            ════════════════════════════════════════════════════════════
//            Project       : ${project.name}
//            Version       : ${project.version}
//            Java Version  : ${java.sourceCompatibility}
//            Spring Boot   : 3.2.1
//            Database      : H2 (In-Memory)
//            ════════════════════════════════════════════════════════════
//        """.trimIndent())
//	}
//}
