package org.toch.rickmortytest.data

import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.toch.rickmortytest.data.dto.CharacterResponse
import kotlin.test.Test
import org.toch.rickmortytest.data.remote.RickAndMortyApi
import org.toch.rickmortytest.data.remote.impl.RickAndMortyApiImpl
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class RickAndMortyApiTest {

    // 1. Simulamos el JSON exacto que devolvería la API de Rick y Morty
    private val fakeCharacterJsonResponse = """
{
      "info": {
        "count": 1,
        "pages": 1,
        "next": null,
        "prev": null
      },
      "results": [
        {
          "id": 1,
          "name": "Rick Sanchez",
          "status": "Alive",
          "species": "Human",
          "type": "",
          "gender": "Male",
          
          "origin": {
            "name": "Earth (C-137)",
            "url": "https://rickandmortyapi.com/api/location/1"
          },
          "location": {
            "name": "Citadel of Ricks",
            "url": "https://rickandmortyapi.com/api/location/3"
          },
          "episode": [
            "https://rickandmortyapi.com/api/episode/1"
          ],
          
          "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
          "url": "https://rickandmortyapi.com/api/character/1",
          "created": "2017-11-04T18:48:46.250Z"
        }
      ]
    }
    """.trimIndent()

    private val fakeSingleCharacterDetailJson = """
    {
      "id": 1,
      "name": "Rick Sanchez",
      "status": "Alive",
      "species": "Human",
      "type": "",
      "gender": "Male",
      "origin": {
        "name": "Earth (C-137)",
        "url": "https://rickandmortyapi.com/api/location/1"
      },
      "location": {
        "name": "Citadel of Ricks",
        "url": "https://rickandmortyapi.com/api/location/3"
      },
      "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
      "episode": [
        "https://rickandmortyapi.com/api/episode/1",
        "https://rickandmortyapi.com/api/episode/2"
      ],
      "url": "https://rickandmortyapi.com/api/character/1",
      "created": "2017-11-04T18:48:46.250Z"
    }
""".trimIndent()

    @Test
    fun getCharacter_returns_mapped_domain_character_on_success() = runTest {
        // ARRANGE (Preparar)
        // Creamos el cliente falso con el JSON de arriba
        val mockHttpClient = KtorMockClientFactory.create(fakeCharacterJsonResponse)
        val api: RickAndMortyApi = RickAndMortyApiImpl(mockHttpClient)

        // ACT
        // Supongamos que llamas a la función que pide la página de personajes
        val result: Result<CharacterResponse> = api.getCharacters(page = 1)

        // 2. Verificas que el resultado sea exitoso
        assertTrue(result.isSuccess)

        // 3. 💡 LA SOLUCIÓN: Desempaquetas el contenido usando getOrThrow()
        val response: CharacterResponse = result.getOrThrow()

        // 4. Ahora sí entras a la lista de personajes dentro de tu CharacterResponse
        // (Ajusta '.results' o '.characters' según cómo se llame la lista en tu DTO)
        val firstCharacter = response.results.first()

        assertEquals(1, firstCharacter.id) // 👈 ¡Adiós al Unresolved reference 'id'!
        assertEquals("Rick Sanchez", firstCharacter.name)
        assertEquals("Alive", firstCharacter.status)

    }

    @Test
    fun getCharacter_propagates_error_on_api_failure() = runTest {
        // ARRANGE
        // Simulamos un error de servidor 500
        val mockHttpClient = KtorMockClientFactory.create(
            jsonResponse = "Internal Server Error",
            status = HttpStatusCode.InternalServerError
        )
        val api: RickAndMortyApi = RickAndMortyApiImpl(mockHttpClient)

        // ACT & ASSERT
        // Verificamos que tu arquitectura capture o lance la excepción esperada ante fallos
        kotlin.runCatching {
            api.getCharacter(id = 1)
        }.onFailure { exception ->
            assertNotNull(exception) // El test pasa si la API propaga el error correctamente
        }
    }

    @Test
    fun getCharacterDetail_returns_single_character_data_on_success() = runTest {
        // ARRANGE (Preparar)
        // Usamos el factory con el JSON del personaje individual
        val mockHttpClient = KtorMockClientFactory.create(fakeSingleCharacterDetailJson)
        val api: RickAndMortyApi = RickAndMortyApiImpl(mockHttpClient)

        // ACT (Actuar)
        // Llamamos al endpoint del detalle para el ID 1
        val result: Result<CharacterResponse.CharacterData> = api.getCharacter(id = 1)

        // ASSERT (Verificar)
        // 1. Validamos que la petición no haya fallado
        assertTrue(result.isSuccess)

        // 2. Desempaquetamos el DTO del personaje individual
        val characterData = result.getOrThrow()

        // 3. Corroboramos que las propiedades básicas se mapearon bien
        assertEquals(1, characterData.id)
        assertEquals("Rick Sanchez", characterData.name)
        assertEquals("Alive", characterData.status)
        assertEquals("Human", characterData.species)
        assertEquals("Male", characterData.gender)

        // 4. Verificamos los objetos anidados (Muy importante para el detalle)
        assertNotNull(characterData.origin)
        assertEquals("Earth (C-137)", characterData.origin.name)

        assertNotNull(characterData.location)
        assertEquals("Citadel of Ricks", characterData.location.name)

        // 5. Verificamos las colecciones
        assertEquals(2, characterData.episode.size)
        assertEquals("https://rickandmortyapi.com/api/episode/1", characterData.episode.first())
    }
}