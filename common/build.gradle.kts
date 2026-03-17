dependencies {
    // Libraries
    implementation("org.lushplugins.chatcolorhandler:paper:7.0.0")
}

tasks {
    shadowJar {
        relocate("org.lushplugins.chatcolorhandler", "org.lushplugins.guihandler.libraries.chatcolorhandler")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString() + ".guihandler"
            artifactId = rootProject.name
            version = rootProject.version.toString()
            from(project.components["java"])
        }
    }
}