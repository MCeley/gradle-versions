# Gradle Versions
Grabs the latest versions of Gradle and AGP and prints the versions to the console.

Can optionally specify an output directory to write the versions out as JSON.

`./gradlew run --args="--out=/path/to/output/dir"`

Sample output:
```json
{
  "gradle": {
    "current": {
      "version": "7.4.2",
      "downloadUrl": "https://services.gradle.org/distributions/gradle-7.4.2-bin.zip"
    },
    "releaseCandidate": {
      "version": "7.5-rc-1",
      "downloadUrl": "https://services.gradle.org/distributions/gradle-7.5-rc-1-bin.zip"
    },
    "nightly": {
      "version": "7.6-20220605220253+0000",
      "downloadUrl": "https://services.gradle.org/distributions-snapshots/gradle-7.6-20220605220253+0000-bin.zip"
    },
    "releaseNightly": {
      "version": "7.5-20220604003603+0000",
      "downloadUrl": "https://services.gradle.org/distributions-snapshots/gradle-7.5-20220604003603+0000-bin.zip"
    }
  },
  "agp": {
    "stable": "7.2.1",
    "releaseCandidate": "7.3.0-rc01 (not a real version)",
    "beta": "7.3.0-beta01",
    "alpha": "7.4.0-alpha02"
  }
}
```
