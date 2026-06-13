package org.toch.rickmortytest.presentation.viewmodel.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.domain.repository.CharacterRepository
import org.toch.rickmortytest.domain.model.CharacterPagingSource
import org.toch.rickmortytest.presentation.viewmodel.viewmodelbehavior.CharacterEffectProvider

class CharacterViewModel(
    private val characterRepository: CharacterRepository,
) : ViewModel(), CharacterEffectProvider {

    private val _sideEffect = Channel<CharacterSideEffect>()
    override val sideEffect = _sideEffect.receiveAsFlow()
    private var characterPagingSource: CharacterPagingSource? = null
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _favoriteOverrides = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val favoriteOverrides: StateFlow<Map<Int, Boolean>> = _favoriteOverrides.asStateFlow()

    private var pendingInternalChanges = 0

    init {
        viewModelScope.launch {
            characterRepository.repositoryChanges.collect {
                if (pendingInternalChanges > 0) {
                    pendingInternalChanges--
                } else { //recreate paging from another one
                    _favoriteOverrides.value = emptyMap()
                    characterPagingSource?.invalidate()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val charactersFlow = _searchQuery
        .debounce(300) // 💡 Espera 300ms para evitar recrear el Pager de forma abrupta si el usuario escribe rápido
        .flatMapLatest { query ->
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    CharacterPagingSource(
                        characterRepository = characterRepository,
                        query = query // 💡 Le pasamos el texto actual al PagingSource
                    ).also { characterPagingSource = it }
                }
            ).flow
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun toggleFavorite(character: Character) {
        val newFavoriteState = !character.isFavorite

        _favoriteOverrides.update { current ->
            current + (character.id to newFavoriteState)
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = if (newFavoriteState) "guardado" else "eliminado"

                pendingInternalChanges++

                if (newFavoriteState) {
                    characterRepository.saveCharacterLocal(character)
                } else {
                    characterRepository.deleteCharacterLocal(character)
                }

                _sideEffect.send(CharacterSideEffect.ShowSnackBar("${character.name} $result"))
            } catch (e: Exception) {
                pendingInternalChanges--
                _favoriteOverrides.update { current ->
                    current + (character.id to character.isFavorite)
                }
                _sideEffect.send(CharacterSideEffect.ShowSnackBar("Error: ${e.message}"))
            }
        }
    }

    fun onCharacterClicked(id: Int) {
        viewModelScope.launch {
            _sideEffect.send(CharacterSideEffect.NavigateToCharacterDetail(id))
        }
    }
}