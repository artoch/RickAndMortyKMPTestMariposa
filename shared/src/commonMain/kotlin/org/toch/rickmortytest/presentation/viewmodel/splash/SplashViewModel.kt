package org.toch.rickmortytest.presentation.viewmodel.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady

    init {
        viewModelScope.launch {
            // Sincroniza o inicializa lo que necesites aquí (BD, API, etc.)
            delay(2000) // Muestra el splash por 2 segundos
            _isReady.value = true
        }
    }
}