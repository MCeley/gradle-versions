package com.michaelceley.versions

import com.michaelceley.versions.util.VersionBuilder
import picocli.CommandLine
import picocli.CommandLine.Option
import java.io.File
import kotlin.system.exitProcess

class VersionFetcher : Runnable {

    @Option(
        names = ["-o", "--out"],
        description = [ "Optional directory location to output all AGP and Gradle versions."],
        paramLabel = "OUTPUT"
    )
    var outputDir: File? = null

    override fun run() = VersionBuilder.exportVersions(outputDir)
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(VersionFetcher()).execute(*args))
