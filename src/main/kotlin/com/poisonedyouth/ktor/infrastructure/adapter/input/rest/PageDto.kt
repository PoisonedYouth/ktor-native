package com.poisonedyouth.ktor.infrastructure.adapter.input.rest

import arrow.core.Either
import arrow.core.raise.either
import com.poisonedyouth.ktor.domain.entity.Page
import com.poisonedyouth.ktor.domain.vo.Failure
import java.util.UUID

data class PageDto(
    val id: UUID,
    val tag: String,
    val title: String,
    val url: String,
    val available: Boolean
)

fun Page.toPageDto(): Either<Failure, PageDto> = either {
    PageDto(
        id = this@toPageDto.id.id().bind(),
        title = this@toPageDto.title.value,
        tag = this@toPageDto.tag,
        url = this@toPageDto.url.toExternalForm(),
        available = this@toPageDto.available
    )
}
