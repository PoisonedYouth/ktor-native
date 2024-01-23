package com.poisonedyouth.ktor.application.usecases

import arrow.core.Either
import com.poisonedyouth.ktor.domain.vo.Failure
import com.poisonedyouth.ktor.domain.entity.Page
import com.poisonedyouth.ktor.domain.vo.Identity
import java.util.UUID

interface PageUseCase {

    suspend fun add(page: Page): Either<Failure, Identity<UUID>>

    suspend fun update(page: Page): Either<Failure, Unit>

    suspend fun delete(identity: Identity<UUID>): Either<Failure, Unit>

    suspend fun find(identity: Identity<UUID>): Either<Failure, Page?>

    suspend fun findAllBy(tag: String): Either<Failure, List<Page>>

    suspend fun getAll(): Either<Failure, List<Page>>
}