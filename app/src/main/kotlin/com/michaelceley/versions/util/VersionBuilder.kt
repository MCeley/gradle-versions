package com.michaelceley.versions.util

import com.google.gson.Gson
import com.michaelceley.versions.model.AndroidGradlePlugin
import com.michaelceley.versions.model.GradleVersionResponse
import com.michaelceley.versions.model.Versions
import com.michaelceley.versions.net.Networking
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException

object VersionBuilder {

    fun exportVersions(outputFile: File? = null) {
        val versions = buildVersions()

        println("Gradle Versions")
        println("Current: ${versions.gradle.current?.version ?: "None"}")
        println("Release Candidate: ${versions.gradle.releaseCandidate?.version ?: "None"}")
        println("Release Nightly: ${versions.gradle.releaseNightly?.version ?: "None"}")
        println("Nightly: ${versions.gradle.nightly?.version ?: "None"}")

        println(" ")

        println("AGP Versions")
        println("Stable: ${versions.agp.stable ?: "None"}")
        println("Release Candidate: ${versions.agp.releaseCandidate ?: "None"}")
        println("Beta: ${versions.agp.beta ?: "None"}")
        println("Alpha: ${versions.agp.alpha ?: "None"}")

        println(Gson().toJson(versions))
    }

    fun buildVersions() : Versions {
        val versionService = Networking.versionService

        val currentGradle = getGradleVersion(versionService.getCurrentGradleVersion())
        val releaseCandidateGradle = getGradleVersion(versionService.getReleaseCandidateGradleVersion())
        val releaseNightlyGradle = getGradleVersion(versionService.getReleaseNightlyGradleVersion())
        val nightlyGradle = getGradleVersion(versionService.getNightlyGradleVersion())

        val agpVersions: AndroidGradlePlugin? = try {
            versionService.getAndroidToolsVersions().execute().body()?.gradlePlugin
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

        return Versions(
            agpVersions?.getLatestStableVersion(),
            agpVersions?.getLatestRCVersion(),
            agpVersions?.getLatestBetaVersion(),
            agpVersions?.getLatestAlphaVersion(),
            currentGradle,
            releaseCandidateGradle,
            nightlyGradle,
            releaseNightlyGradle
        )
    }

    private fun getGradleVersion(call: Call<GradleVersionResponse?>) : GradleVersionResponse? {
        var response: Response<GradleVersionResponse?>? = null
        try {
            response = call.execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response?.body()
    }
}