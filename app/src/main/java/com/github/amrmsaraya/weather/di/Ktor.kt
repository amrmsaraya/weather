package com.github.amrmsaraya.weather.di

import android.util.Log
import com.github.amrmsaraya.weather.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Ktor {

    private val client = HttpClient(Android) {

        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(HttpTimeout){
            connectTimeoutMillis = 5000
            requestTimeoutMillis = 5000
            socketTimeoutMillis = 5000
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Logger Ktor", message)
                }
            }
            level = LogLevel.BODY
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status", "${response.status.value}")
            }
        }

        install(DefaultRequest) {
            host = BuildConfig.BASE_URL
            url {
                protocol = URLProtocol.HTTPS
            }

            parameter("appid", BuildConfig.API_KEY)
            parameter("exclude", "minutely")

            header(HttpHeaders.ContentType, ContentType.Application.Json)

        }
    }

    @Singleton
    @Provides
    fun provideKtorClient(): HttpClient {
        return client
    }
}
