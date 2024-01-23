package com.poisonedyouth.ktor.infrastructure.adapter.input.thymeleaf

import arrow.core.Either
import com.poisonedyouth.ktor.application.usecases.PageUseCase
import com.poisonedyouth.ktor.domain.vo.Failure
import com.poisonedyouth.ktor.infrastructure.adapter.input.rest.toPageDto
import com.poisonedyouth.ktor.infrastructure.server.injector
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.thymeleaf.ThymeleafContent

fun Application.registerPageOverviewRoutes() {
    val pageUseCase = injector.getInstance(PageUseCase::class.java)

    routing {
        route("/index") {
            get {
                when (val result = pageUseCase.getAll()) {
                    is Either.Left -> call.respondWithFailure(result.value)
                    is Either.Right -> {
                        call.respond(
                            ThymeleafContent(
                                "index", mapOf("pages" to result.value
                                    .map {
                                        when (val pageDto = it.toPageDto()) {
                                            is Either.Left -> call.respondWithFailure(pageDto.value)
                                            is Either.Right -> pageDto.value
                                        }
                                    })
                            )
                        )
                    }
                }

            }
        }
    }
}

private suspend fun ApplicationCall.respondWithFailure(failure: Failure) {
    this.respond(HttpStatusCode.InternalServerError, failure.message)
}