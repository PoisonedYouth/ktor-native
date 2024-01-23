package com.poisonedyouth.ktor.infrastructure.adapter.output.exposed.transaction

import arrow.core.Either
import com.poisonedyouth.ktor.domain.vo.Failure
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> transactionWithRollbackOnFailure(
    db: Database? = null,
    statement: Transaction.() -> Either<Failure, T>
): Either<Failure, T> =
    newSuspendedTransaction(db = db) {
        statement().onLeft {
            this.rollback()
        }
    }
