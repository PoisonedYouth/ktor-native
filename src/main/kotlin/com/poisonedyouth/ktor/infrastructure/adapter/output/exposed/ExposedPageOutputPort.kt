package com.poisonedyouth.ktor.infrastructure.adapter.output.exposed

import arrow.core.Either
import arrow.core.raise.either
import com.poisonedyouth.ktor.application.ports.output.PageOutputPort
import com.poisonedyouth.ktor.domain.entity.Page
import com.poisonedyouth.ktor.domain.vo.ExistingIdentity
import com.poisonedyouth.ktor.domain.vo.Failure
import com.poisonedyouth.ktor.domain.vo.Identity
import com.poisonedyouth.ktor.domain.vo.NoIdentity
import com.poisonedyouth.ktor.domain.vo.Title
import com.poisonedyouth.ktor.domain.vo.eval
import com.poisonedyouth.ktor.domain.vo.toIdentity
import com.poisonedyouth.ktor.infrastructure.adapter.output.exposed.table.PageTable
import com.poisonedyouth.ktor.infrastructure.adapter.output.exposed.transaction.transactionWithRollbackOnFailure
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.net.URL
import java.time.Instant
import java.util.UUID

class ExposedPageOutputPort : PageOutputPort {
    override suspend fun save(page: Page): Either<Failure, Identity<UUID>> = transactionWithRollbackOnFailure {
        val now = Instant.now()
        either {
            when (page.id) {
                is NoIdentity -> {
                    eval {
                        PageTable.insertAndGetId {
                            it[title] = page.title.value
                            it[tag] = page.tag
                            it[url] = page.url.toExternalForm()
                            it[available] = page.available
                            it[created] = now
                            it[updated] = now
                        }.value.toIdentity()
                    }.bind()
                }

                is ExistingIdentity -> {
                    eval {
                        PageTable.update({ PageTable.id eq page.id.id().bind() }) {
                            it[title] = page.title.value
                            it[tag] = page.tag
                            it[url] = page.url.toExternalForm()
                            it[available] = page.available
                            it[updated] = now
                        }
                        page.id
                    }.bind()
                }

                else -> raise(Failure.InvalidStateFailure("Not supported identity type."))
            }
        }
    }

    override suspend fun findBy(identity: Identity<UUID>): Either<Failure, Page?> = transactionWithRollbackOnFailure {
        either {
            eval {
                PageTable.selectAll().where {
                    PageTable.id eq identity.id().bind()
                }.firstOrNull()?.toPage()?.bind()
            }.bind()
        }
    }

    override suspend fun getAll(): Either<Failure, List<Page>> = transactionWithRollbackOnFailure {
        either {
            eval {
                PageTable.selectAll().map { it.toPage().bind() }
            }.bind()
        }
    }

    override suspend fun delete(identity: Identity<UUID>): Either<Failure, Unit> = transactionWithRollbackOnFailure {
        either {
            eval {
                PageTable.deleteWhere {
                    PageTable.id eq identity.id().bind()
                }
            }.bind()
        }
    }

    override suspend fun findAllBy(tag: String): Either<Failure, List<Page>> = transactionWithRollbackOnFailure {
        either {
            eval {
                PageTable.selectAll().where {
                    PageTable.tag like tag
                }.map { it.toPage().bind() }
            }.bind()
        }
    }

    private fun ResultRow.toPage(): Either<Failure, Page> = either {
        Page(
            id = this@toPage[PageTable.id].value.toIdentity(),
            title = Title.of(this@toPage[PageTable.title]).bind(),
            tag = this@toPage[PageTable.tag],
            url = URL(this@toPage[PageTable.url]),
            available = this@toPage[PageTable.available]

        )
    }
}