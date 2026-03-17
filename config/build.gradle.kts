dependencies {
    // Dependencies
    compileOnly(project(":common"))

    // Libraries
    api("org.lushplugins.lushlib:item:1.0.0")
    implementation("org.lushplugins.lushlib:config:1.0.0")
    implementation("org.lushplugins.lushlib:common:1.0.0")
    implementation("org.lushplugins.lushlib:utils:1.0.0")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString() + ".guihandler"
            artifactId = rootProject.name + "-" + project.name
            version = rootProject.version.toString()
            from(project.components["java"])
        }
    }
}