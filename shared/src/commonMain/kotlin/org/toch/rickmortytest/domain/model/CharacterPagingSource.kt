package org.toch.rickmortytest.domain.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.toch.rickmortytest.domain.repository.CharacterRepository

class CharacterPagingSource(
    private val characterRepository: CharacterRepository
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
            var characters = characterRepository.getCharactersFromDb(page)

            if (characters.isEmpty()) {
                // Si esto falla, saltará directamente al bloque catch
                characterRepository.syncPage(page)

                // Volvemos a consultar la DB ahora que tiene datos
                characters = characterRepository.getCharactersFromDb(page)
            }

            LoadResult.Page(
                data = characters,
                prevKey = if (page == 1) null else page - 1,
                // Si la API no devolvió datos en la sincronización,
                // significa que llegamos al final de las páginas.
                nextKey = if (characters.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            // Paging de Jetpack intercepta esto y expone el estado de error a la UI
            LoadResult.Error(exception)
        }
    }
}