package org.toch.rickmortytest.presentation.viewmodel.localcharacter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.domain.repository.CharacterRepository
import org.toch.rickmortytest.presentation.viewmodel.character.CharacterSideEffect
import org.toch.rickmortytest.presentation.viewmodel.viewmodelbehavior.CharacterEffectProvider

class LocalCharacterViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel(), CharacterEffectProvider {

    private val _state = MutableStateFlow(LocalCharacterState())
    val state: StateFlow<LocalCharacterState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sideEffect = Channel<CharacterSideEffect>()
    override val sideEffect = _sideEffect.receiveAsFlow()

    init {
        observeCharacters()
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeCharacters() {
        viewModelScope.launch {
            _searchQuery
                .debounce(200) // 💡 Espera 200ms para no saturar la DB mientras el usuario escribe rápido
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    _state.update { it.copy(isLoading = true) }

                    if (query.isBlank()) { // search all local data
                        characterRepository.getLocalCharacters()
                    } else {
                        characterRepository.searchLikeCharacterByNameDb(name = query)
                    }
                }
                .flowOn(Dispatchers.IO) // Nos aseguramos de que la DB se consulte en el hilo de fondo
                .catch { error ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Error local")
                    }
                }
                .collect { charactersList ->
                    // Actualizamos el estado con los resultados en tiempo real
                    _state.update {
                        it.copy(
                            isLoading = false,
                            characters = charactersList,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun removeFromFavorites(character: Character) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val name = character.name

                characterRepository.deleteCharacterLocal(character)

                _sideEffect.send(CharacterSideEffect.ShowSnackBar("$name eliminado"))

            } catch (e: Exception) {
                _sideEffect.send(CharacterSideEffect.ShowSnackBar("No se pudo eliminar"))
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCharacterClicked(id: Int) {
        viewModelScope.launch {
            _sideEffect.send(CharacterSideEffect.NavigateToCharacterDetail(id))
        }
    }
}