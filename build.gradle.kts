plugins {
    scala
    id("com.github.johnrengelman.shadow") version "6.1.0"
    `maven-publish`
}

group = properties["projectGroup"]!!
version = properties["projectVersion"]!!

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation("org.scala-lang:scala-library:2.13.4")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.4-R0.1-SNAPSHOT")
}

val shade = configurations.create("shade")
shade.extendsFrom(configurations.implementation.get())

tasks {
    javadoc {
        options.encoding = "UTF-8"
    }

    compileScala {
        scalaCompileOptions.isOptimize = true
    }

    create<Jar>("sourceJar") {
        archiveClassifier.set("source")
        from(sourceSets["main"].allSource)
    }

    processResources {
        filesMatching("*.yml") {
            expand(project.properties)
        }
    }

    jar {
        from ( shade.map { if (it.isDirectory) it else zipTree(it) } )
    }
}

publishing {
    publications {
        create<MavenPublication>("Clear_Shell") {
            artifact(tasks["sourceJar"])
            from(components["java"])
        }
    }
}