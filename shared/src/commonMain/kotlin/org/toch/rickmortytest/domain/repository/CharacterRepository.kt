package org.toch.rickmortytest.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.domain.model.CharacterPaging

interface CharacterRepository {

    val repositoryChanges: SharedFlow<Unit>
    suspend fun notifyChange()
    suspend fun getCharacters(page: Int): Result<CharacterPaging>

    suspend fun getCharacter(id: Int): Result<Character>

    fun getLocalCharacters(): Flow<List<Character>>

    suspend fun saveCharacterLocal(character: Character)

    suspend fun deleteCharacterLocal(character: Character)

    suspend fun getCharactersFromDb(
        page:Int
    ): List<Character>

    suspend fun getCharactersFromDb(
        name:String
    ): List<Character>

    suspend fun searchLikeCharacterByNameDb(name: String): Flow<List<Character>>
    suspend fun syncSearchByName(page:Int,name: String)

    suspend fun syncPage(
        page:Int
    )
}