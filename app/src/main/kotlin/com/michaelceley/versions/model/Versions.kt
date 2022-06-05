package com.michaelceley.versions.model

class Versions(
    agpStable: AndroidToolsVersion?,
    agpRC: AndroidToolsVersion?,
    agpBeta: AndroidToolsVersion?,
    agpAlpha: AndroidToolsVersion?,
    gradleCurrent: GradleVersionResponse?,
    gradleRC: GradleVersionResponse?,
    gradleNightly: GradleVersionResponse?,
    gradleReleaseNightly: GradleVersionResponse?
) {

    data class GradleVersions(
        val current: GradleVersionResponse?,
        val releaseCandidate: GradleVersionResponse?,
        val nightly: GradleVersionResponse?,
        val releaseNightly: GradleVersionResponse?
    )

    data class AGPVersions(
        val stable: String?,
        val releaseCandidate: String?,
        val beta: String?,
        val alpha: String?
    )

    val gradle = GradleVersions(gradleCurrent, gradleRC, gradleNightly, gradleReleaseNightly)
    val agp = AGPVersions(agpStable?.versionString, agpRC?.versionString, agpBeta?.versionString, agpAlpha?.versionString)
}