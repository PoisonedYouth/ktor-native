package com.poisonedyouth.ktor.domain.vo

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure

@JvmInline
value class Title private constructor(
    val value: String
) {
    companion object {
        fun of(value: String): Either<Failure, Title> = either {
            ensure(value.isNotEmpty()) {
                Failure.ValidationFailure("Title must not be empty.")
            }
            Title(value)
        }
    }
}
