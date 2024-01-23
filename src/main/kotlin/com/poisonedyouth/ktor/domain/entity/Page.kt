package com.poisonedyouth.ktor.domain.entity

import com.poisonedyouth.ktor.domain.vo.Identity
import com.poisonedyouth.ktor.domain.vo.NoIdentity
import com.poisonedyouth.ktor.domain.vo.Title
import java.net.URL
import java.util.UUID

data class Page(
    val id: Identity<UUID> = NoIdentity,
    val title: Title,
    val tag: String,
    val url: URL,
    val available: Boolean
)
