package org.toch.rickmortytest.domain.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.toch.rickmortytest.domain.repository.CharacterRepository

class CharacterPagingSource(
    private val characterRepository: CharacterRepository,
    private val query: String? = null
) : PagingSource<Int, Character>() {

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Character> {

        val page = params.key ?: 1

        return try {
            var characters: List<Character>

            if (!query.isNullOrBlank()) {
                // 🔍 1. Intentamos buscar primero en la base de datos local (FTS4)
                characters = characterRepository.getCharactersFromDb(query)

                if (characters.isEmpty()) {
                    characterRepository.syncSearchByName(page,query)
                    characters = characterRepository.getCharactersFromDb(query)
                }
            } else {
                characters = characterRepository.getCharactersFromDb(page)

                if (characters.isEmpty()) {
                    characterRepository.syncPage(page)
                    characters = characterRepository.getCharactersFromDb(page)
                }
            }


            LoadResult.Page(
                data = characters,
                prevKey = if (page == 1) null else page - 1,
                // Si la API no devolvió datos en la sincronización,
                // significa que llegamos al final de las páginas.
                nextKey = if (characters.isEmpty() || !query.isNullOrBlank()) null else page + 1
            )
        } catch (exception: Exception) {
            // Paging de Jetpack intercepta esto y expone el estado de error a la UI
            LoadResult.Error(exception)
        }
    }
}