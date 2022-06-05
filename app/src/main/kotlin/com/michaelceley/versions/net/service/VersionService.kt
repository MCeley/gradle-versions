package com.michaelceley.versions.net.service

import com.michaelceley.versions.model.AndroidToolsVersionResponse
import com.michaelceley.versions.model.GradleVersionResponse
import retrofit2.Call
import retrofit2.http.GET

interface VersionService {

    /**
     * API call for getting the Gradle nightly version.
     *
     * @return A [Call] object for executing the API call.
     */
    @GET("https://services.gradle.org/versions/nightly")
    fun getNightlyGradleVersion(): Call<GradleVersionResponse?>

    /**
     * API call for getting the Gradle nightly version for the next release.
     *
     * @return A [Call] object for executing the API call.
     */
    @GET("https://services.gradle.org/versions/release-nightly")
    fun getReleaseNightlyGradleVersion(): Call<GradleVersionResponse?>

    /**
     * API call for getting the current Gradle release candidate version.
     *
     * @return A [Call] object for executing the API call.
     */
    @GET("https://services.gradle.org/versions/release-candidate")
    fun getReleaseCandidateGradleVersion(): Call<GradleVersionResponse?>

    /**
     * API call for getting the current version of Gradle.
     *
     * @return A [Call] object for executing the API call.
     */
    @GET("https://services.gradle.org/versions/current")
    fun getCurrentGradleVersion(): Call<GradleVersionResponse?>

    /**
     * API call for getting a listing of all versions of the Android tools.
     * We use the response of this call to parse out just the AGP version we need.
     *
     * @return A [Call] object for executing the API call.
     */
    @GET("https://dl.google.com/android/maven2/com/android/tools/build/group-index.xml")
    fun getAndroidToolsVersions(): Call<AndroidToolsVersionResponse?>
}