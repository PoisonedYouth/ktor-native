package com.poisonedyouth.ktor.application.ports.output

import arrow.core.Either
import com.poisonedyouth.ktor.domain.entity.Page
import com.poisonedyouth.ktor.domain.vo.Failure
import com.poisonedyouth.ktor.domain.vo.Identity
import java.util.UUID

interface PageOutputPort {

    suspend fun save(page: Page): Either<Failure, Identity<UUID>>

    suspend fun findBy(identity: Identity<UUID>): Either<Failure, Page?>

    suspend fun findAllBy(tag: String): Either<Failure, List<Page>>

    suspend fun getAll(): Either<Failure, List<Page>>

    suspend fun delete(identity: Identity<UUID>): Either<Failure, Unit>
}