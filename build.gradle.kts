import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream

plugins {
	id("org.springframework.boot") version "2.6.4"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	id("com.ncorti.ktfmt.gradle") version "0.11.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

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
		jvmTarget = "11"
	}
}

ktfmt {
	// Google style - 2 space indentation
	googleStyle()
	// Breaks lines longer than maxWidth. Default 100.
	maxWidth.set(80)
	removeUnusedImports.set(true)
}

tasks.withType<Test> {
	useJUnitPlatform()
}


tasks.register<com.ncorti.ktfmt.gradle.tasks.KtfmtFormatTask>("ktfmtPrecommit") {
	source = project.fileTree(rootDir)
	println("Start source formatting using ktfmt")
	val files =
		ByteArrayOutputStream()
			.use { outputStream ->
				exec {
					commandLine = listOf("git", "diff", "--name-only", "--cached", "--diff-filter=ACMRTUXB")
					standardOutput = outputStream
				}
				outputStream.toString()
			}
			.split("\r\n", "\n")
			.filter { it.endsWith(".kt") }
	setIncludes(HashSet(files))
}


tasks.register("addKtfmtPrecommitHook") {
	//copy the pre-commit file under scripts folder into /.git/hooks/ folder
	doLast {
		val preCommitHook = project.file(".git/hooks/pre-commit")
		if (!preCommitHook.exists()) {
			val file = project.file("scripts/pre-commit")
			file.copyTo(preCommitHook, overwrite = true)
		}
	}
}

tasks.getByName("build").dependsOn("addKtfmtPrecommitHook")
tasks.getByName("bootJar").dependsOn("addKtfmtPrecommitHook")



