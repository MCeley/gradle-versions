package com.michaelceley.versions.model

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * Model class for getting the current Android tools versions, specifically for the Gradle plugin.
 * <br><br>
 * XML Structure (unused properties excluded):
 * <pre>
 * {@code
 * <com.android.tools.build>
 *   <gradle versions="7.1.4,7.2.0-beta03,7.3.0-alpha07"></gradle>
 * </com.android.tools.build>
 * }
 * </pre>
 */
@XmlRootElement(name = "com.android.tools.build")
data class AndroidToolsVersionResponse(
    @XmlElement(name = "gradle")
    var gradlePlugin: AndroidGradlePlugin? = null
)

class AndroidGradlePlugin {

    @XmlAttribute
    var versions: String? = null

    private var latestStableVersion: AndroidToolsVersion? = null
    private var latestBetaVersion: AndroidToolsVersion? = null
    private var latestAlphaVersion: AndroidToolsVersion? = null
    private var latestRCVersion: AndroidToolsVersion? = null
    private var allVersions: List<AndroidToolsVersion>? = null

    private fun filterVersions() {
        if (allVersions != null) {
            return
        }

        allVersions = versions?.split(",")?.map { AndroidToolsVersion(it) }
        allVersions?.let {
            for (version in it) {
                when(version.type) {
                    VersionType.ALPHA -> latestAlphaVersion = version
                    VersionType.BETA -> latestBetaVersion = version
                    VersionType.RC -> latestRCVersion = version
                    VersionType.STABLE -> latestStableVersion = version
                }
            }
        }

        // Only show RC, beta, and alpha versions beyond the latest stable
        latestStableVersion?.let {
            if((latestRCVersion?.compareTo(it) ?: -1) < 0) {
                latestRCVersion = null
            }
            if((latestBetaVersion?.compareTo(it) ?: -1) < 0) {
                latestBetaVersion = null
            }
            if((latestAlphaVersion?.compareTo(it) ?: -1) < 0) {
                latestAlphaVersion = null
            }
        }
    }

    fun getLatestAlphaVersion(): AndroidToolsVersion? {
        filterVersions()
        return latestAlphaVersion
    }

    fun getLatestBetaVersion(): AndroidToolsVersion? {
        filterVersions()
        return latestBetaVersion
    }

    fun getLatestRCVersion(): AndroidToolsVersion? {
        filterVersions()
        return latestRCVersion
    }

    fun getLatestStableVersion(): AndroidToolsVersion? {
        filterVersions()
        return latestStableVersion
    }
}

