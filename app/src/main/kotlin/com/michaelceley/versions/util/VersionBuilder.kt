package com.michaelceley.versions.util

import com.google.gson.Gson
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

object VersionBuilder {

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