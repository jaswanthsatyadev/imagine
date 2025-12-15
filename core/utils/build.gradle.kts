plugins {
    alias(libs.plugins.image.toolbox.library)
    alias(libs.plugins.image.toolbox.compose)
    alias(libs.plugins.image.toolbox.hilt)
}

android.namespace = "com.evolvarc.imagine.core.utils"

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.resources)
    implementation(projects.core.settings)
    "marketImplementation"(libs.quickie.bundled)
    "fossImplementation"(libs.quickie.foss)
    implementation(libs.zxing.android.embedded)
}