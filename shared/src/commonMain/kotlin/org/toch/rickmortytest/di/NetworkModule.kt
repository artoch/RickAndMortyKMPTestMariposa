package org.toch.rickmortytest.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.toch.rickmortytest.data.remote.RickAndMortyApi
import org.toch.rickmortytest.data.remote.impl.RickAndMortyApiImpl

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
            defaultRequest {
                url("https://rickandmortyapi.com/api/")
            }
        }
    }
    single<RickAndMortyApi> { RickAndMortyApiImpl(get()) }
}