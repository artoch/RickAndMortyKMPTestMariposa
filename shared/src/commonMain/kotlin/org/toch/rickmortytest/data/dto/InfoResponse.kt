package org.toch.rickmortytest.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class InfoResponse(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?,
)