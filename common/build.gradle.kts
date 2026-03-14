dependencies {
    // Libraries
    implementation("org.lushplugins:ChatColorHandler:6.0.4")
}

tasks {
    shadowJar {
        relocate("org.lushplugins.chatcolorhandler", "org.lushplugins.guihandler.libraries.chatcolorhandler")
    }
}