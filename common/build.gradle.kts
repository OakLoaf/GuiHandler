dependencies {
    // Libraries
    implementation("org.lushplugins.chatcolorhandler:paper:7.0.0")
}

tasks {
    shadowJar {
        relocate("org.lushplugins.chatcolorhandler", "org.lushplugins.guihandler.libraries.chatcolorhandler")
    }
}