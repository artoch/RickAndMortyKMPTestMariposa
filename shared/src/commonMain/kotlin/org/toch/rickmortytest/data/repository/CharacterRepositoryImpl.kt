package org.toch.rickmortytest.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import org.toch.rickmortytest.data.mapper.toCharacter
import org.toch.rickmortytest.data.remote.RickAndMortyApi
import org.toch.rickmortytest.database.RickAndMortyDatabaseTest
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.domain.model.CharacterPaging
import org.toch.rickmortytest.domain.repository.CharacterRepository
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map

class CharacterRepositoryImpl(
    private val api: RickAndMortyApi,
    private val database: RickAndMortyDatabaseTest
): CharacterRepository {

    private val queries = database.characterTableQueries

    private val _repositoryChanges = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val repositoryChanges = _repositoryChanges.asSharedFlow()

    override suspend fun notifyChange() {
        _repositoryChanges.emit(Unit)
    }

    override suspend fun getCharacters(page: Int): Result<CharacterPaging> =
        api.getCharacters(page).map { response ->
            CharacterPaging(
                characters = response.results.map { dto ->
                    val character = dto.toCharacter()

                    val isSaved = queries.isCharacterFavorite(character.id.toLong())
                        .executeAsOne() > 0

                    character.copy(isFavorite = isSaved)
                },
                canLoadMore = response.info.next != null,
            )
        }

    override suspend fun getCharacter(id: Int): Result<Character> = api.getCharacter(id).map { it.toCharacter() }

    // 1. OBTENER FAVORITOS EN TIEMPO REAL
    override fun getLocalCharacters(): Flow<List<Character>> {
        return queries.getFavoriteCharacter()
            .asFlow()                 // Convierte la query de SQLDelight a un Flow
            .mapToList(Dispatchers.IO) // Ejecuta la lectura en el hilo de Entrada/Salida de forma segura
            .map { entities ->        // Mapeamos la lista de entidades de la DB al modelo de Dominio
                entities.map { entity ->
                    Character(
                        id = entity.id.toInt(),
                        name = entity.name,
                        status = entity.status,
                        image = entity.imageUrl,
                        species = entity.species,
                        gender = entity.gender,
                        location = entity.location,
                        type = entity.type,
                        isFavorite = entity.isFavorite == 1L
                    )
                }
            }
    }

    // 2. GUARDAR PERSONAJE EN LA BASE DE DATOS
    override suspend fun saveCharacterLocal(character: Character) {
        // SQLDelight por defecto maneja los IDs como Long, por lo que convertimos el Int a Long

        queries.updateFavoriteStatus(
            1L,
            character.id.toLong()
        )

        notifyChange()
    }

    override suspend fun getCharactersFromDb(
        page:Int
    ): List<Character> {

        return queries
            .getCharactersByPage(page.toLong())
            .executeAsList()
            .map {
                Character(
                    id = it.id.toInt(),
                    name = it.name,
                    status = it.status,
                    species = it.species,
                    gender = it.gender,
                    location = it.location,
                    type = it.type,
                    image = it.imageUrl,
                    isFavorite = it.isFavorite == 1L
                )

            }

    }

    override suspend fun syncPage(page:Int){

        api.getCharacters(page).onSuccess {

            it.results.forEach { character ->

                queries.insertCharacter(
                    id = character.id.toLong(),
                    page = page.toLong(),
                    name = character.name,
                    status = character.status,
                    species = character.species,
                    gender = character.gender,
                    location = character.location.name,
                    type = character.type,
                    imageUrl = character.image,
                    isFavorite = 0
                )

            }

        }

    }

    // 3. ELIMINAR PERSONAJE DE LA BASE DE DATOS
    override suspend fun deleteCharacterLocal(character: Character) {
        queries.updateFavoriteStatus(
            0L,
            character.id.toLong()
        )
        notifyChange()
    }
}