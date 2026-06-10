package org.toch.rickmortytest.local

import app.cash.sqldelight.db.SqlDriver
import junit.framework.TestCase.assertEquals
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.toch.rickmortytest.database.CharacterTableQueries
import org.toch.rickmortytest.database.RickAndMortyDatabaseTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [35])
class CharacterDatabaseTest {

    private lateinit var driver: SqlDriver
    private lateinit var database: RickAndMortyDatabaseTest
    private lateinit var queries: CharacterTableQueries

    @BeforeTest
    fun setup() {
        driver = createInMemorySqlDriver()
        database = RickAndMortyDatabaseTest.Companion(driver)
        queries = database.characterTableQueries
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    @Test
    fun insertAndGetFavoriteCharacter() {
        queries.insertCharacter(1L, 1,"Rick Sanchez", "Alive", "Human","","","", "url_image", isFavorite = 1L)
        val favorites = queries.getFavoriteCharacter().executeAsList()

        assertEquals(1, favorites.size)
        assertEquals("Rick Sanchez", favorites.first().name)
    }

    @Test
    fun delete_character_removes_it_from_favorites_successfully() {
        // 1. ARRANGE: Preparamos e insertamos un personaje de prueba
        val characterId = 2L
        queries.insertCharacter(
            id = characterId,
            page = 1,
            name = "Morty Smith",
            status = "Alive",
            species = "Human",
            gender = "",
            location = "",
            type = "",
            imageUrl = "https://url-falsa.com/morty.jpeg",
            isFavorite = 1L
        )

        // Verificación intermedia de seguridad: Aseguramos que se guardó correctamente
        val beforeDelete = queries.getFavoriteCharacter().executeAsList()
        assertEquals(1, beforeDelete.size, "El personaje debió haberse insertado primero")

        // 2. ACT: Ejecutamos la acción de eliminar
        // 💡 Ajusta 'deleteCharacter' al nombre exacto que le pusiste a tu query en el archivo .sq
        queries.deleteCharacter(id = characterId)

        // 3. ASSERT: Verificamos que el personaje ya no exista en la base de datos
        val afterDelete = queries.getFavoriteCharacter().executeAsList()

        // Comprobamos que la lista regresó a estar 100% vacía
        assertTrue(afterDelete.isEmpty(), "La lista de favoritos debería estar vacía después de eliminar")
    }

}