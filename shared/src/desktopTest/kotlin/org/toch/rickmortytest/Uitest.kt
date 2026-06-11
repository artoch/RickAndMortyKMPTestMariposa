package org.toch.rickmortytest
//
//import androidx.compose.material3.Text
//import androidx.compose.ui.test.ExperimentalTestApi
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.v2.runComposeUiTest
//import org.toch.rickmortytest.presentation.screen.character.remote.item.CharacterItem
//import kotlin.test.Test
//
//class UiTestDesk {
//
//    @OptIn(ExperimentalTestApi::class)
//    @Test
//    fun pokemonTest() = runComposeUiTest {
//        CharacterItem()
//        setContent {
//            Text("hola")
//        }
//        onNodeWithText("hola").assertExists()
//    }
//
//}