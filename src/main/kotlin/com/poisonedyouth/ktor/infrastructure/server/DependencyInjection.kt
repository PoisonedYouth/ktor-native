package com.poisonedyouth.ktor.infrastructure.server

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Provides
import com.poisonedyouth.ktor.application.ports.input.PageInputPort
import com.poisonedyouth.ktor.application.ports.output.PageOutputPort
import com.poisonedyouth.ktor.application.usecases.PageUseCase
import com.poisonedyouth.ktor.infrastructure.adapter.output.exposed.ExposedPageOutputPort
import io.ktor.server.application.Application

lateinit var injector: Injector

fun Application.setupDependencyInjection() {
    injector = Guice.createInjector(MainModule())
}

class MainModule : AbstractModule() {

    @Provides
    fun pageOutputPort(): PageOutputPort {
        return ExposedPageOutputPort()
    }

    @Provides
    fun pageUseCase(pageOutputPort: PageOutputPort): PageUseCase {
        return PageInputPort(pageOutputPort)
    }

    @Provides
    fun pageCrawler(pageUseCase: PageUseCase): PageCrawler {
        return PageCrawler(pageUseCase)
    }
}