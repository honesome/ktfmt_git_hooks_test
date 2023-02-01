import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	id("com.ncorti.ktfmt.gradle") version "0.11.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

ktfmt {
	// Google style - 2 space indentation
	googleStyle()
	// Breaks lines longer than maxWidth. Default 100.
	maxWidth.set(80)
	removeUnusedImports.set(true)
}

tasks.register<KtfmtFormatTask>("ktfmtPrecommit") {
	source = project.fileTree(rootDir)
	include("**/*.kt")
}


tasks.withType<Test> {
	useJUnitPlatform()
}
