package com.michaelceley.versions.net.converter

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import javax.annotation.Nullable


internal class QualifiedTypeConverterFactory(private val jsonFactory: Converter.Factory,
                                             private val xmlFactory: Converter.Factory)
    : Converter.Factory() {

    @Nullable
    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation?>, retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        for (annotation in annotations) {
            if (annotation is Json) {
                return jsonFactory.responseBodyConverter(type, annotations, retrofit)
            }
            if (annotation is Xml) {
                return xmlFactory.responseBodyConverter(type, annotations, retrofit)
            }
        }
        return null
    }

    @Nullable
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation?>,
        methodAnnotations: Array<Annotation?>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        for (annotation in parameterAnnotations) {
            if (annotation is Json) {
                return jsonFactory.requestBodyConverter(
                    type, parameterAnnotations, methodAnnotations, retrofit
                )
            }
            if (annotation is Xml) {
                return xmlFactory.requestBodyConverter(
                    type, parameterAnnotations, methodAnnotations, retrofit
                )
            }
        }
        return null
    }
}