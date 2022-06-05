package com.michaelceley.versions.net

import com.michaelceley.versions.net.converter.QualifiedTypeConverterFactory
import com.michaelceley.versions.net.service.VersionService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jaxb.JaxbConverterFactory

/**
 * Holder for all networking setup
 */
object Networking {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://example.com") // Requires a baseUrl be set even if not used.
            .addConverterFactory(QualifiedTypeConverterFactory(
                GsonConverterFactory.create(),
                JaxbConverterFactory.create()
            ))
            .build()
    }

    val versionService: VersionService by lazy {
        retrofit.create(VersionService::class.java)
    }
}