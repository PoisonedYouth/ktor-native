package com.poisonedyouth.ktor.application.ports.input

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import com.poisonedyouth.ktor.application.ports.output.PageOutputPort
import com.poisonedyouth.ktor.application.usecases.PageUseCase
import com.poisonedyouth.ktor.domain.entity.Page
import com.poisonedyouth.ktor.domain.vo.Failure
import com.poisonedyouth.ktor.domain.vo.Identity
import com.poisonedyouth.ktor.domain.vo.NoIdentity
import java.util.UUID

class PageInputPort(
    private val outputPort: PageOutputPort
) : PageUseCase {
    override suspend fun add(page: Page): Either<Failure, Identity<UUID>> = either {
        ensure(page.id is NoIdentity) {
            Failure.InvalidStateFailure("Page with id '${page.id}' already exists.")
        }
        outputPort.save(page).bind()
    }

    override suspend fun update(page: Page): Either<Failure, Unit> = either {
        outputPort.save(page).bind()
        Unit
    }

    override suspend fun delete(identity: Identity<UUID>): Either<Failure, Unit> = either {
        val existingPage = outputPort.findBy(identity).bind()
        ensureNotNull(existingPage) {
            Failure.InvalidStateFailure("Page with id '${identity.id()}' does not exists.")
        }
        outputPort.delete(identity).bind()
    }

    override suspend fun find(identity: Identity<UUID>): Either<Failure, Page?> {
        return outputPort.findBy(identity)
    }

    override suspend fun getAll(): Either<Failure, List<Page>> {
        return outputPort.getAll()
    }

    override suspend fun findAllBy(tag: String): Either<Failure, List<Page>> {
        return outputPort.findAllBy(tag)
    }
}