package com.poisonedyouth.ktor.infrastructure.adapter.input.rest.failure

import com.poisonedyouth.ktor.domain.vo.Failure
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond


suspend fun ApplicationCall.respondWithFailure(failure: Failure) {
    when (failure) {
        is Failure.GenericFailure -> this.respond(HttpStatusCode.InternalServerError, failure.message)
        is Failure.InvalidStateFailure -> this.respond(HttpStatusCode.InternalServerError, failure.message)
        is Failure.ValidationFailure -> this.respond(HttpStatusCode.BadRequest, failure.message)
    }
}