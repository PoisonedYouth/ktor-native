package com.poisonedyouth.ktor.infrastructure.adapter.input.rest

import arrow.core.Either
import com.poisonedyouth.ktor.application.usecases.PageUseCase
import com.poisonedyouth.ktor.domain.vo.Failure
import com.poisonedyouth.ktor.infrastructure.adapter.input.rest.failure.respondWithFailure
import com.poisonedyouth.ktor.infrastructure.server.PageCrawler
import com.poisonedyouth.ktor.infrastructure.server.injector
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.registerPageRoutes() {
    val pageUseCase = injector.getInstance(PageUseCase::class.java)
    val pageCrawler = injector.getInstance(PageCrawler::class.java)

    routing {
        route("/page") {
            post("") {
                val amount = call.request.queryParameters["amount"]
                    ?: return@post call.respondWithFailure(
                        Failure.ValidationFailure("Missing parameter 'amount'.")
                    )
                val query = call.request.queryParameters["query"] ?: return@post call.respondWithFailure(
                    Failure.ValidationFailure("Missing parameter 'query'.")
                )
                pageCrawler.createPages(
                    amount = amount.toInt(),
                    query = query
                )
                call.respond(HttpStatusCode.Accepted)
            }
            get {
                when (val result = pageUseCase.getAll()) {
                    is Either.Left -> call.respondWithFailure(result.value)
                    is Either.Right -> call.respond(HttpStatusCode.OK, result.value
                        .map {
                            when (val pageDto = it.toPageDto()) {
                                is Either.Left -> call.respondWithFailure(pageDto.value)
                                is Either.Right -> pageDto.value
                            }
                        })
                }
            }
            get("/bytag") {
                val tag = call.request.queryParameters["tag"]
                    ?: return@get call.respondWithFailure(
                        Failure.ValidationFailure("Missing parameter 'tag'.")
                    )
                when (val result = pageUseCase.findAllBy(tag)) {
                    is Either.Left -> call.respondWithFailure(result.value)
                    is Either.Right -> call.respond(HttpStatusCode.OK, result.value
                        .map {
                            when (val pageDto = it.toPageDto()) {
                                is Either.Left -> call.respondWithFailure(pageDto.value)
                                is Either.Right -> pageDto.value
                            }
                        })
                }
            }

        }
    }
}
