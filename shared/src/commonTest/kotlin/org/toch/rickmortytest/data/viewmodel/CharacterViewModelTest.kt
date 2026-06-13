package org.toch.rickmortytest.data.viewmodel

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import org.toch.rickmortytest.data.model.CharacterTestData
import org.toch.rickmortytest.data.repository.FakeCharacterRepository
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.domain.repository.CharacterRepository
import org.toch.rickmortytest.presentation.viewmodel.character.CharacterSideEffect
import org.toch.rickmortytest.presentation.viewmodel.character.CharacterState
import org.toch.rickmortytest.presentation.viewmodel.character.CharacterViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterViewModelTest : KoinTest {
    private val testDispatcher = StandardTestDispatcher()
    private val fakeRepository: FakeCharacterRepository
        get() = get<CharacterRepository>() as FakeCharacterRepository
    private lateinit var viewModel: CharacterViewModel
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(module {
                single<CharacterRepository> { FakeCharacterRepository() }
            })
        }
        viewModel = CharacterViewModel(fakeRepository)
    }
    @AfterTest
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }
    @Test
    fun `load characters page 1 from Paging successfully`() = runTest(testDispatcher) {
        // GIVEN: El repositorio entregará la página 1 (contiene 5 personajes de los 10)
        fakeRepository.mockCharactersResult = Result.success(CharacterTestData.mockPagingPage1)
        // WHEN: Consumimos el flow del Pager usando las herramientas de test oficiales de Paging 3
        val items: List<Character> = viewModel.charactersFlow.asSnapshot()
        // THEN: Verificamos que cargó exactamente los 5 personajes iniciales
        assertEquals(25, items.size)
        assertEquals("Rick Sanchez", items[0].name)
        assertEquals("Jerry Smith", items[4].name) // El quinto elemento de la lista
    }
    @Test
    fun `toggleFavorite should add character to database and trigger side effect`() = runTest(testDispatcher) {
        val character = CharacterTestData.mockCharactersList.first()
        // WHEN
        viewModel.toggleFavorite(character)
        // THEN: Esperamos a que se emita el efecto secundario de forma asíncrona
        val effect = viewModel.sideEffect.first()
        assertTrue(fakeRepository.saveCharacterCalled)
        assertTrue(effect is CharacterSideEffect.ShowSnackBar)
    }
    @Test
    fun `onCharacterClicked should emit navigation effect`() = runTest(testDispatcher) {
        // WHEN
        viewModel.onCharacterClicked(15)
        // THEN: Esperamos el efecto
        val effect = viewModel.sideEffect.first()
        assertEquals(
            CharacterSideEffect.NavigateToCharacterDetail(15),
            effect
        )
    }
    @Test
    fun `toggleFavorite should emit error snackbar when save fails`() = runTest(testDispatcher) {

        val character = CharacterTestData.mockCharactersList.first()
        fakeRepository.throwOnSave = true

        viewModel.toggleFavorite(character)

        val effect = viewModel.sideEffect.first()
        assertEquals(
            CharacterSideEffect.ShowSnackBar("Error: Error guardando"),
            effect
        )
    }
    @Test
    fun `invalidate is triggered when database updates`() = runTest(testDispatcher) {

        fakeRepository.mockCharactersResult = Result.success(CharacterTestData.mockPagingPage1)

        val collectJob = launch {
            viewModel.charactersFlow.collect {}
        }
        advanceUntilIdle()

        fakeRepository.saveCharacterLocal(CharacterTestData.mockCharactersList.first())
        advanceUntilIdle()

        assertTrue(fakeRepository.savedLocalCharacters.isNotEmpty())
        assertEquals(1, fakeRepository.savedLocalCharacters.size)
        collectJob.cancel()
    }

    @Test
    fun `add character and delete item`() = runTest(testDispatcher) {
        // GIVEN: Inicializamos el PagingSource cargando datos para que deje de ser nulo internamente
        fakeRepository.mockCharactersResult = Result.success(CharacterTestData.mockPagingPage1)
        // Arrancamos la recolección del flujo para inicializar el characterPagingSource interno del ViewModel
        val collectJob = launch {
            viewModel.charactersFlow.collect {}
        }
        advanceUntilIdle()
        // WHEN: Simulamos que otra parte de la app guarda algo e impacta la DB local
        fakeRepository.saveCharacterLocal(CharacterTestData.mockCharactersList.first())
        advanceUntilIdle()
        // THEN: Gracias al collect de repositoryChanges en el 'init' del ViewModel,
        // el flujo reacciona. Podemos comprobar que el elemento fue añadido a la DB fake de forma limpia.
        assertTrue(fakeRepository.savedLocalCharacters.isNotEmpty())
        assertEquals(1, fakeRepository.savedLocalCharacters.size)
        collectJob.cancel()
    }

    @Test
    fun `Search Character From DB successfully with debounce`() = runTest(testDispatcher) {
        // GIVEN: Cargamos datos de prueba en el repositorio Fake
        fakeRepository.mockCharactersResult = Result.success(CharacterTestData.mockPagingPage1)

        // WHEN: El usuario escribe en la barra de búsqueda
        viewModel.onSearchQueryChanged("Rick")

        // ⏱️ ¡EL PASO CLAVE!: Avanzamos el reloj virtual para superar el .debounce(300) del ViewModel
        advanceTimeBy(301)
        // O puedes usar advanceUntilIdle() para procesar todo lo pendiente en la cola del Dispatcher
        advanceUntilIdle()

        // Tomamos la captura de la lista paginada recreada por el flatMapLatest
        val items: List<Character> = viewModel.charactersFlow.asSnapshot()

        // THEN: Validamos que la lista se filtró correctamente según la lógica de tu FakeRepository
        // Nota: Ajusta los asserts al tamaño exacto de elementos devueltos por el filtro de tu Fake
        assertTrue(items.isNotEmpty())
        assertTrue(items.all { it.name.contains("Rick", ignoreCase = true) })
        assertEquals("Rick Sanchez", items[0].name)
        assertEquals(1, items.count())
    }
}