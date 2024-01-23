package com.poisonedyouth.ktor.domain.vo

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.utils.io.core.toByteArray
import java.util.UUID

interface Identity<out T> {
    fun id(): Either<Failure, T>
}

object NoIdentity : Identity<Nothing> {
    override fun id(): Either<Failure, Nothing> {
        return Failure.InvalidStateFailure("Id not yet set.").left()
    }
}

@JvmInline
value class ExistingIdentity<T>(
    private val value: T
) : Identity<T> {
    override fun id(): Either<Failure, T> {
        return value.right()
    }
}

fun String.toIdentity(): ExistingIdentity<UUID> {
    return ExistingIdentity(UUID.fromString(this))
}

fun UUID.toIdentity(): ExistingIdentity<UUID> {
    return ExistingIdentity(this)
}

fun newUUIDIdentity(): ExistingIdentity<UUID> {
    return UUID.randomUUID().toIdentity()
}

fun UUID.toByteArray(): ByteArray = this.toString().toByteArray()
