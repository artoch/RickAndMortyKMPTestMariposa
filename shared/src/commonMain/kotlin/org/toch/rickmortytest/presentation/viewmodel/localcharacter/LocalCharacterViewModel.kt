package org.toch.rickmortytest.presentation.viewmodel.localcharacter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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

    private val _sideEffect = Channel<CharacterSideEffect>()
    override val sideEffect = _sideEffect.receiveAsFlow()

    init {
        loadLocalCharacters()
    }

    private fun loadLocalCharacters() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            characterRepository.getLocalCharacters()
                .flowOn(Dispatchers.IO)
                .catch { error ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Error local")
                    }
                }
                .collect { charactersList ->
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

    fun onCharacterClicked(id: Int) {
        viewModelScope.launch {
            _sideEffect.send(CharacterSideEffect.NavigateToCharacterDetail(id))
        }
    }
}