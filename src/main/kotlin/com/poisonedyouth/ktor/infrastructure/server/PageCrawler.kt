package com.poisonedyouth.ktor.infrastructure.server

import arrow.core.Either
import arrow.core.raise.either
import com.jayway.jsonpath.JsonPath
import com.poisonedyouth.ktor.application.usecases.PageUseCase
import com.poisonedyouth.ktor.domain.entity.Page
import com.poisonedyouth.ktor.domain.vo.Failure
import com.poisonedyouth.ktor.domain.vo.Identity
import com.poisonedyouth.ktor.domain.vo.Title
import com.poisonedyouth.ktor.domain.vo.eval
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.UUID

private const val searchAmount: Int = 10

class PageCrawler(
    private val pageUseCase: PageUseCase
) {
    private val httpClient: HttpClient = HttpClient.newHttpClient()

    suspend fun createPages(amount: Int, query: String): Either<Failure, List<Identity<UUID>>> = coroutineScope {
        either {
            buildList {
                coroutineScope {
                    repeat(amount) {
                        launch {
                            val request =
                                HttpRequest.newBuilder(
                                    URI.create(
                                        "https://www.googleapis.com/customsearch/v1?key=AIzaSyCOuQKLbuhWneB2tpvuHoiIoBLBJ10J308&cx=9751ddeca925b4316&q=$query" +
                                                "&num=$searchAmount&start=${searchAmount + it * searchAmount}"
                                    )
                                ).GET().build()
                            eval {
                                val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
                                val resultList: List<LinkedHashMap<String, String>> =
                                    JsonPath.parse(response.body()).read("$.items[*]")
                                resultList.forEach {
                                    launch {
                                        val id = pageUseCase.add(
                                            Page(
                                                title = Title.of(it.getValueOrRaise("title").bind()).bind(),
                                                tag = query,
                                                url = URL(it.getValueOrRaise("link").bind()),
                                                available = true,
                                            )
                                        ).bind()
                                        add(id)
                                    }
                                }
                            }.bind()
                        }
                    }
                }
            }
        }
    }

    private fun LinkedHashMap<String, String>.getValueOrRaise(key: String): Either<Failure, String> = either {
        this@getValueOrRaise[key] ?: raise(Failure.InvalidStateFailure("Required key '$key' not found."))
    }

}