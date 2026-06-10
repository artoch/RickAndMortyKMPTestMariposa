package org.toch.rickmortytest.data.mapper

import org.toch.rickmortytest.data.dto.CharacterResponse
import org.toch.rickmortytest.domain.model.Character

fun CharacterResponse.CharacterData.toCharacter(): Character =
    Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        location = location.name,
        image = image,
    )