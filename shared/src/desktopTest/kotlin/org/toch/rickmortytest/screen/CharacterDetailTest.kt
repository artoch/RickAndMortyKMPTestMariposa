package org.toch.rickmortytest.screen

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.presentation.screen.character.remote.item.CharacterItem
import org.toch.rickmortytest.presentation.screen.characterdetail.CharacterDetailContent
import org.toch.rickmortytest.presentation.screen.characterdetail.CharacterDetailScreen
import org.toch.rickmortytest.presentation.viewmodel.characterdetail.CharacterDetailState
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        // 🛸 1. Le asignamos al hilo principal el dispatcher de pruebas antes de que corra el test
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        // 🧹 2. Limpiamos el hilo principal al terminar para no ensuciar otros tests
        Dispatchers.resetMain()
    }


    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `character is null in the view`() = runComposeUiTest {
        setContent {
            CharacterDetailContent(
                state = CharacterDetailState(
                    isLoading = false,
                    character = null,
                    errorMessage = null
                ),
                onNavigateBack = {}
            )
        }
        onNodeWithText("Rick Sanchez").assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `character is exist and check field`() = runComposeUiTest {
        setContent {
            CharacterDetailContent(
                state = CharacterDetailState(
                    isLoading = false,
                    character =  Character(
                        id = 1,
                        name = "Rick Sanchez",
                        status = "Alive",
                        species = "Human",
                        type = "Human",
                        gender = "Male",
                        location = "Citadel of Ricks",
                        image = "",
                        isFavorite = true
                    ),
                    errorMessage = null
                ),
                onNavigateBack = {}
            )
        }
        onAllNodesWithText("Rick Sanchez").assertCountEquals(2)
        onAllNodesWithText("Human").assertCountEquals(1)
        onNodeWithText("Male").assertExists()
        onNodeWithText("Citadel of Ricks").assertExists()
    }

}