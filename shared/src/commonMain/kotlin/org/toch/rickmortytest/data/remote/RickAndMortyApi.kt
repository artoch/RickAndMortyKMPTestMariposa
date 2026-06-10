package org.toch.rickmortytest.data.remote

import org.toch.rickmortytest.data.dto.CharacterResponse

interface RickAndMortyApi {
    suspend fun getCharacters(page: Int): Result<CharacterResponse>

    suspend fun getCharacter(id: Int): Result<CharacterResponse.CharacterData>
}