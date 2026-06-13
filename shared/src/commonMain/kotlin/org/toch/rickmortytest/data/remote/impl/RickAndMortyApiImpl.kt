package org.toch.rickmortytest.data.remote.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.toch.rickmortytest.data.dto.CharacterResponse
import org.toch.rickmortytest.data.remote.RickAndMortyApi

class RickAndMortyApiImpl(
    private val httpClient: HttpClient
): RickAndMortyApi {

    override suspend fun getCharacters(page: Int): Result<CharacterResponse> =
        safeApiCall {
            httpClient.get("character") {
                parameter("page", page)
            }
        }

    override suspend fun getCharacters(page: Int, name: String): Result<CharacterResponse> =
        safeApiCall {
            httpClient.get("character") {
                parameter("page", page)
                parameter("name", name)
            }
        }

    override suspend fun getCharacter(id: Int): Result<CharacterResponse.CharacterData> =
        safeApiCall {
            httpClient.get("character/$id")
        }

    suspend inline fun <reified T> safeApiCall(crossinline body: suspend () -> HttpResponse): Result<T> =
        runCatching {
            withContext(Dispatchers.IO) {
                val response = body()
                if (response.status == HttpStatusCode.TooManyRequests) {
                    throw Exception(TOO_MANY_REQUEST_MESSAGE)
                }
                response.body<T>()
            }
        }

    companion object Companion {
        const val TOO_MANY_REQUEST_MESSAGE = "Hubo un error, realizaste muchas llamadas al servidor. " +
                "Espera un momento y vuelve a intentarlo"
    }
}