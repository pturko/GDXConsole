val gdxVersion by extra {"1.11.0"}
val gdxVfxVersion by extra {"0.5.1"}
val jUnitPlatformVersion by extra {"1.9.0"}
val jUnitJupiterVersion by extra {"5.9.0"}
val mockitoVersion by extra {"4.11.0"}
val assertJVersion by extra {"3.23.1"}
val logbackVersion by extra {"1.4.5"}
val commonsLang3Version by extra {"3.12.0"}
val lombokVersion by extra {"1.18.26"}
val ashleyVersion by extra {"1.7.4"}
val box2DLightsVersion by extra {"1.5"}

plugins {
    java
    `java-library`
}

allprojects {

    version = "0.1.0"

    apply(plugin = "java")
    apply(plugin = "java-library")

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation("org.junit.platform:junit-platform-launcher:$jUnitPlatformVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitJupiterVersion")
        testImplementation("org.mockito:mockito-core:$mockitoVersion")
        testImplementation("org.assertj:assertj-core:$assertJVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiterVersion")
    }
}

project(":desktop") {

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(project(":core"))
        implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
        implementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")
        implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
        implementation("com.badlogicgames.ashley:ashley:$ashleyVersion")
    }
}

project(":core") {

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
        implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
        implementation("com.crashinvaders.vfx:gdx-vfx-core:$gdxVfxVersion")
        implementation("com.crashinvaders.vfx:gdx-vfx-effects:$gdxVfxVersion")
        implementation("ch.qos.logback:logback-classic:$logbackVersion")
        testImplementation("com.badlogicgames.gdx:gdx-backend-headless:$gdxVersion")
        implementation("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
        implementation("com.badlogicgames.box2dlights:box2dlights:$box2DLightsVersion")
        implementation("com.badlogicgames.ashley:ashley:$ashleyVersion")
        implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
        compileOnly("org.projectlombok:lombok:$lombokVersion")
        annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    }

}
