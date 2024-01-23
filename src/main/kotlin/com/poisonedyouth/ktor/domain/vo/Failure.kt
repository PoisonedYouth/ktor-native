package com.poisonedyouth.ktor.domain.vo

import arrow.core.Either

sealed interface Failure {
    val message: String

    data class ValidationFailure(override val message: String) : Failure

    data class InvalidStateFailure(override val message: String) : Failure

    data class GenericFailure(val e: Throwable) : Failure {
        override val message: String = e.localizedMessage
    }
}

fun <T> eval(exec: () -> T): Either<Failure, T> {
    return Either.catch {
        exec()
    }.mapLeft {
        Failure.GenericFailure(it)
    }
}
