package com.michaelceley.versions.net

import com.michaelceley.versions.net.service.VersionService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jaxb.JaxbConverterFactory

object Networking {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://example.com") // Requires a baseUrl be set even if not used.
            .addConverterFactory(JaxbConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val versionService by lazy {
        retrofit.create(VersionService::class.java)
    }
}