package org.toch.rickmortytest.data.model

import org.toch.rickmortytest.domain.model.Character
import org.toch.rickmortytest.domain.model.CharacterPaging

object CharacterTestData {

    val mockCharactersList = listOf(
        Character(
            id = 1, name = "Rick Sanchez", status = "Alive", species = "Human",
            type = "", gender = "Male", location = "Citadel of Ricks",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg", isFavorite = false
        ),
        Character(
            id = 2, name = "Morty Smith", status = "Alive", species = "Human",
            type = "", gender = "Male", location = "Earth (Replacement Dimension)",
            image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg", isFavorite = true
        ),
        Character(
            id = 3, name = "Summer Smith", status = "Alive", species = "Human",
            type = "", gender = "Female", location = "Earth (Replacement Dimension)",
            image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg", isFavorite = false
        ),
        Character(
            id = 4, name = "Beth Smith", status = "Alive", species = "Human",
            type = "", gender = "Female", location = "Earth (Replacement Dimension)",
            image = "https://rickandmortyapi.com/api/character/avatar/4.jpeg", isFavorite = false
        ),
        Character(
            id = 5, name = "Jerry Smith", status = "Alive", species = "Human",
            type = "", gender = "Male", location = "Earth (Replacement Dimension)",
            image = "https://rickandmortyapi.com/api/character/avatar/5.jpeg", isFavorite = false
        ),
        Character(
            id = 6, name = "Abadango Cluster Princess", status = "Alive", species = "Alien",
            type = "", gender = "Female", location = "Abadango",
            image = "https://rickandmortyapi.com/api/character/avatar/6.jpeg", isFavorite = false
        ),
        Character(
            id = 7, name = "Abradolf Lincler", status = "unknown", species = "Human",
            type = "Genetic experiment", gender = "Male", location = "Testicle Monster Dimension",
            image = "https://rickandmortyapi.com/api/character/avatar/7.jpeg", isFavorite = false
        ),
        Character(
            id = 8, name = "Birdperson", status = "Alive", species = "Alien",
            type = "Bird-Person", gender = "Male", location = "Planet Squanch",
            image = "https://rickandmortyapi.com/api/character/avatar/8.jpeg", isFavorite = true
        ),
        Character(
            id = 9, name = " thereal-Mr. Poopybutthole", status = "Alive", species = "unknown",
            type = "", gender = "Male", location = "Earth (Unknown dimension)",
            image = "https://rickandmortyapi.com/api/character/avatar/12.jpeg", isFavorite = false
        ),
        Character(
            id = 10, name = "Squanchy", status = "Alive", species = "Alien",
            type = "Cat-like", gender = "Male", location = "Planet Squanch",
            image = "https://rickandmortyapi.com/api/character/avatar/15.jpeg", isFavorite = false
        )
    )

    // Simula la primera página (Devuelve los primeros 5 ítems y dice que hay más)
    val mockPagingPage1 = CharacterPaging(
        characters = mockCharactersList.take(5),
        canLoadMore = true
    )

    // Simula la segunda página (Devuelve los siguientes 5 ítems y avisa que ya no hay más)
    val mockPagingPage2 = CharacterPaging(
        characters = mockCharactersList.drop(5).take(5),
        canLoadMore = false
    )
}