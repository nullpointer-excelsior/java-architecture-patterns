plugins {
	java
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.benjamin"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
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
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.postgresql:postgresql")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging.showStandardStreams = true
	testLogging.showStackTraces = true
	/*testLogging {
		testLogging.showExceptions = true
	}*/
}

val databaseName by extra("hexagonal-example")


tasks.register("databaseStart", Exec::class) {
	group = "Infrastructure"
	description = "Start the application PostgreSQL database"
	commandLine("docker", "run", "-d", "--name", databaseName,
		"-e", "POSTGRES_USER=$databaseName",
		"-e", "POSTGRES_PASSWORD=$databaseName",
		"-e", "POSTGRES_DB=$databaseName",
		"-p", "5432:5432", "postgres:latest")

}

tasks.register("databaseStop", Exec::class) {
	group = "Infrastructure"
	description = "Stop and remove the application PostgreSQL database"
	commandLine("docker", "rm", "-f", databaseName)
}


