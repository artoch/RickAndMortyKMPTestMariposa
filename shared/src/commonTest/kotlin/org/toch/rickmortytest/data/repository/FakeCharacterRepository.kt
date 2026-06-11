package org.toch.rickmortytest.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOf
import org.toch.rickmortytest.data.model.CharacterTestData.mockCharactersList
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.domain.model.CharacterPaging
import org.toch.rickmortytest.domain.repository.CharacterRepository
class FakeCharacterRepository : CharacterRepository {

    // Simula la emisión de cambios de la DB
    private val _repositoryChanges = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val repositoryChanges: SharedFlow<Unit> = _repositoryChanges.asSharedFlow()

    var saveCharacterCalled = false

    var throwOnSave = false

    // Datos controlados para los tests
    var mockCharactersResult: Result<CharacterPaging>? = Result.failure(Exception("Not initialized"))
    val savedLocalCharacters = mutableListOf<Character>()

    var resultToBeReturned: Result<Character> = Result.success(mockCharactersList.first())

    fun clear() {
        mockCharactersResult = null
        throwOnSave = false
        saveCharacterCalled = false
        savedLocalCharacters.clear()
    }

    override suspend fun notifyChange() {
        _repositoryChanges.emit(Unit)
    }

    override suspend fun getCharacters(page: Int): Result<CharacterPaging> {
        return mockCharactersResult ?: Result.failure(Exception("Mock no configurado para este test"))
    }

    override suspend fun saveCharacterLocal(character: Character) {


        if (throwOnSave) {
            throw Exception("Error guardando")
        }

        savedLocalCharacters.add(character)
        notifyChange()

        saveCharacterCalled = true
    }

    override suspend fun getCharacter(id: Int): Result<Character> {
        return resultToBeReturned
    }

    override fun getLocalCharacters(): Flow<List<Character>> {
        return flowOf(savedLocalCharacters)
    }

    override suspend fun deleteCharacterLocal(character: Character) {
        savedLocalCharacters.removeAll { it.id == character.id }
        notifyChange()
    }

    override suspend fun getCharactersFromDb(page: Int): List<Character> {
        return mockCharactersResult?.getOrNull()?.characters?.toList() ?: emptyList()
    }

    override suspend fun syncPage(page: Int) {
        TODO("Not yet implemented")
    }
}