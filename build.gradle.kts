// Top-level build file. All plugins and repositories are centrally managed in settings.gradle.kts

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
