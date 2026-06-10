package org.toch.rickmortytest.presentation.viewmodel.characterdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.toch.rickmortytest.domain.repository.CharacterRepository

class CharacterDetailViewModel(
    private val characterRepository: CharacterRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Extraemos el ID usando la sintaxis de Jetpack Navigation Type-Safe (se busca por la clase/propiedad)
    // Nota: Si usas la versión más reciente de Navigation, puedes obtenerlo directamente
    // mapeando la ruta completa, pero mantener el acceso por llave "id" funciona perfectamente.
    private val characterId: Int = requireNotNull(savedStateHandle["id"]) {
        "Character id is required"
    }

    // 1. Estado de la pantalla (Reemplaza a reduce)
    private val _state = MutableStateFlow(CharacterDetailState())
    val state: StateFlow<CharacterDetailState> = _state.asStateFlow()

    // 2. Canal para Efectos Secundarios (Reemplaza a postSideEffect)
    private val _sideEffect = Channel<CharacterDetailSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        getCharacter()
    }

    private fun getCharacter() {
        viewModelScope.launch {
            // Marcamos el estado de carga inicial
            _state.update { it.copy(isLoading = true) }

            characterRepository
                .getCharacter(characterId)
                .onSuccess { character ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            character = character,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            character = null,
                            errorMessage = error.message ?: "Hubo un error",
                        )
                    }
                }
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _sideEffect.send(CharacterDetailSideEffect.NavigateBack)
        }
    }
}