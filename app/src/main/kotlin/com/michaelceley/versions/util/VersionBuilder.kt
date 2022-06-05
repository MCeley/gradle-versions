package com.michaelceley.versions.util

import com.google.gson.GsonBuilder
import com.michaelceley.versions.model.AndroidGradlePlugin
import com.michaelceley.versions.model.GradleVersionResponse
import com.michaelceley.versions.model.Versions
import com.michaelceley.versions.net.Networking
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.PrintStream
import java.nio.file.Files

/**
 * Utility class for gathering all AGP and Gradle versions and routing
 * them to the console and/or a file.
 */
object VersionBuilder {

    /**
     * Gathers all the relevant Gradle and AGP versions, writes them out to the
     * console, and optionally writes them to a file as JSON.
     */
    fun exportVersions(outputDir: File? = null) {
        val versions = buildVersions()

        printVersions(versions)

        if(outputDir != null) {
            if (!outputDir.exists()) {
                Files.createDirectory(outputDir.toPath())
            } else if (!outputDir.isDirectory) {
                println("${outputDir.absolutePath} is not a directory.")
                return
            }

            val outputFile = File(outputDir, "versions.json")
            PrintStream(Files.newOutputStream(outputFile.toPath())).use {
                it.append(GsonBuilder().setPrettyPrinting().create().toJson(versions))
            }
        }

    }

    /**
     * Makes all the necessary API calls to gather versions and aggregates them
     * into a single object to be written out to the console and/or a file.
     */
    private fun buildVersions() : Versions {
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

    /**
     * Helper function to write out all versions of Gradle and AGP to the console.
     * @param versions The aggregated versions to write out.
     */
    private fun printVersions(versions: Versions) {
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
    }

    /**
     * Helper function for making the Gradle version API call safely.
     * @param call The API call to make.
     */
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