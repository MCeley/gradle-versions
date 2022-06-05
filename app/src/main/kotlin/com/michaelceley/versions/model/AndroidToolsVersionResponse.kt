package com.michaelceley.versions.model

import javax.xml.bind.annotation.*

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
@XmlAccessorType(XmlAccessType.FIELD)
class AndroidToolsVersionResponse() {
    @XmlElement(name = "gradle")
    var gradlePlugin: AndroidGradlePlugin? = null
}

@XmlAccessorType(XmlAccessType.FIELD)
class AndroidGradlePlugin {

    @XmlAttribute
    var versions: String? = null

    @XmlTransient
    private var latestStableVersion: AndroidToolsVersion? = null
    @XmlTransient
    private var latestBetaVersion: AndroidToolsVersion? = null
    @XmlTransient
    private var latestAlphaVersion: AndroidToolsVersion? = null
    @XmlTransient
    private var latestRCVersion: AndroidToolsVersion? = null
    @XmlTransient
    private var allVersions: List<AndroidToolsVersion>? = null

    private fun filterVersions() {
        if (allVersions != null) {
            return
        }

        allVersions = versions?.split(",")?.map { AndroidToolsVersion(it) }
        allVersions?.let {
            for (version in it) {
                when(version.type) {
                    VersionType.ALPHA -> if((latestAlphaVersion?.compareTo(version) ?: -1) < 0) latestAlphaVersion = version
                    VersionType.BETA -> if((latestBetaVersion?.compareTo(version) ?: -1) < 0) latestBetaVersion = version
                    VersionType.RC -> if((latestRCVersion?.compareTo(version) ?: -1) < 0) latestRCVersion = version
                    VersionType.STABLE -> if((latestStableVersion?.compareTo(version) ?: -1) < 0) latestStableVersion = version
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

