plugins {
	java
	id("org.springframework.boot") version "3.5.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.freefair.lombok") version "8.12"
}

group = "com.poseablesoftware"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
	mavenLocal()
}

val moshiVersion = "1.15.1"
val okioVersion = "3.9.1"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-json")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation(files("device-timeline-lib-v1.4.jar"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
	implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
	implementation("com.squareup.okio:okio:$okioVersion")
	implementation("io.netty:netty-resolver-dns-native-macos")
	implementation("io.github.coordinates2country:coordinates2country:1.9")
	implementation("nl.big-o:atlas:0.2.1")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
