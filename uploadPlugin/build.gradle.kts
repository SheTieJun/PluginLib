plugins {
    `kotlin-dsl`
    `maven-publish`
    `java-gradle-plugin`
}

apply(from = "uploadLocal.gradle")
//apply(from = "PGYbintrayConfig.gradle")

dependencies {
    api(gradleApi())
    api(localGroovy())
    api("com.android.tools.build:gradle:4.0.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.2")
}


repositories {
    jcenter()
    mavenCentral()
    google()
}

