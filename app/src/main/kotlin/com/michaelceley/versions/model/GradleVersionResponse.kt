package com.michaelceley.versions.model

/**
 * Model class for getting version data from the Gradle API.
 * <br><br>
 * JSON Structure (unused properties excluded):
 * <pre>
 * {@code
 * {
 *   "version" : "7.4.1",
 *   "downloadUrl" : "https://services.gradle.org/distributions/gradle-7.4.1-bin.zip"
 * }
 * }
 * </pre>
 */
data class GradleVersionResponse(
    val version: String?,
    val downloadUrl: String?
)
