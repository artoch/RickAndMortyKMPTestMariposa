package org.toch.rickmortytest.screen

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.presentation.screen.character.remote.item.CharacterItem
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterItemTest {

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
    fun pokemonTest() = runComposeUiTest {
        setContent {
            CharacterItem(
                character = Character(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    type = "Human",
                    gender = "Male",
                    location = "Earth",
                    image = "",
                    isFavorite = true
                ),
                onCharacterClick = {

                },
                onFavoriteClick = {

                },
                modifier = Modifier

            )
        }
        onNodeWithText("Rick Sanchez").assertExists()
        onNode(hasText("Human", substring = true)).assertExists()
    }

}