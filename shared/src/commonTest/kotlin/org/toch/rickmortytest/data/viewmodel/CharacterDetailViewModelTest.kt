package org.toch.rickmortytest.data.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.toch.rickmortytest.data.model.CharacterTestData.mockCharactersList
import org.toch.rickmortytest.data.repository.FakeCharacterRepository
import org.toch.rickmortytest.domain.repository.CharacterRepository
import org.toch.rickmortytest.presentation.viewmodel.characterdetail.CharacterDetailViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailViewModelTest : KoinTest {
    private val testDispatcher = StandardTestDispatcher()

    val savedStateHandle = SavedStateHandle(mapOf("id" to 1))
    private lateinit var fakeRepository: FakeCharacterRepository
    private lateinit var viewModel: CharacterDetailViewModel
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(module {
                single<CharacterRepository> { FakeCharacterRepository() }
            })
        }
        fakeRepository = FakeCharacterRepository()

    }
    @AfterTest
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `getCharacter success updates state to character data`() = runTest(testDispatcher) {
        // GIVEN: El repositorio devolverá un personaje exitosamente
        val expectedCharacter = mockCharactersList.first()
        fakeRepository.resultToBeReturned = Result.success(expectedCharacter)

        // WHEN: Se inicializa el ViewModel (dispara getCharacter en el init)
        viewModel = CharacterDetailViewModel(fakeRepository, savedStateHandle)

        // Forzamos a que se ejecuten las corrutinas pendientes en el scheduler
        advanceUntilIdle()

        // THEN: El estado final debe reflejar el éxito
        assertEquals(false, viewModel.state.value.isLoading)
        assertEquals(expectedCharacter, viewModel.state.value.character)
        assertEquals(null, viewModel.state.value.errorMessage)
    }

    @Test
    fun `getCharacter failure updates state with error message`() = runTest(testDispatcher) {
        // GIVEN: El repositorio devolverá un error
        val errorMessage = "Error al conectar con el servidor"
        fakeRepository.resultToBeReturned = Result.failure(Exception(errorMessage))

        // WHEN: Inicializamos el ViewModel
        viewModel = CharacterDetailViewModel(fakeRepository, savedStateHandle)

        // Esperamos a que termine el procesamiento asíncrono
        advanceUntilIdle()

        // THEN: El estado debe contener el mensaje de error y el personaje nulo
        assertEquals(false, viewModel.state.value.isLoading)
        assertEquals(null, viewModel.state.value.character)
        assertEquals(errorMessage, viewModel.state.value.errorMessage)
    }

}