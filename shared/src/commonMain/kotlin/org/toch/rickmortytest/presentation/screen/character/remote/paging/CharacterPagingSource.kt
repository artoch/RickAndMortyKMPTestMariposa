package org.toch.rickmortytest.presentation.screen.character.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.toch.rickmortytest.domain.model.Character
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

        var characters =
            characterRepository.getCharactersFromDb(page)

        if (characters.isEmpty()) {

            characterRepository.syncPage(page)

            characters =
                characterRepository.getCharactersFromDb(page)
        }

        return LoadResult.Page(
            data = characters,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (characters.isEmpty()) null else page + 1
        )
    }
}