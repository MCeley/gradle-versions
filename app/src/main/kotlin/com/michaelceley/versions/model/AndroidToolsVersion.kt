package com.michaelceley.versions.model

/**
 * Breaks an AGP version down into component parts to allow for
 * more granular comparison of versions.
 */
class AndroidToolsVersion(val versionString: String) : Comparable<AndroidToolsVersion> {

    var majorVersion: Int = 0
    var minorVersion: Int = 0
    var patchVersion: Int = 0
    lateinit var type: VersionType
    var typeVersion: Int = 0

    init {
        fun parseVersions(numPart: String) {
            val splits = numPart.split(".")
            for(i in 0..splits.size) {
                when(i) {
                    0 -> majorVersion = splits[i].toInt()
                    1 -> minorVersion = splits[i].toInt()
                    2 -> patchVersion = splits[i].toInt()
                }
            }
        }

        if(!versionString.contains("-")) {
            parseVersions(versionString)
            type = VersionType.STABLE
        } else {
            val splits = versionString.split("-")
            if(splits.size == 2) {
                parseVersions(splits[0])
                type = if(splits[1].contains("alpha")) {
                    VersionType.ALPHA
                } else if(splits[1].contains("beta")) {
                    VersionType.BETA
                } else {
                    VersionType.RC
                }

                typeVersion = splits[1].filter { it.isDigit() }.toInt()
            }
        }
    }

    override fun compareTo(other: AndroidToolsVersion): Int {
        if(majorVersion == other.majorVersion) {
            if(minorVersion == other.minorVersion) {
                if(patchVersion == other.patchVersion) {
                    if(type == other.type) {
                        return typeVersion.compareTo(other.typeVersion)
                    }
                }
                return patchVersion.compareTo(other.patchVersion)
            }
            return minorVersion.compareTo(other.minorVersion)
        }
        return majorVersion.compareTo(other.majorVersion)
    }
}

enum class VersionType {
    ALPHA,
    BETA,
    RC,
    STABLE
}
