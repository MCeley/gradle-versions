/*
 * Copyright (C) 2016 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.michaelceley.versions.net.converter

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import javax.annotation.Nullable

/**
 * Converter factory that allows both JSON and XML serialization of retrofit
 * responses based on annotated response types.
 */
class QualifiedTypeConverterFactory(private val jsonFactory: Converter.Factory,
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