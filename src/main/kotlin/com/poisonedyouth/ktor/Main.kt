package com.poisonedyouth.ktor

import com.poisonedyouth.ktor.infrastructure.adapter.input.rest.registerPageRoutes
import com.poisonedyouth.ktor.infrastructure.adapter.input.thymeleaf.configureThymeleaf
import com.poisonedyouth.ktor.infrastructure.adapter.input.thymeleaf.registerPageOverviewRoutes
import com.poisonedyouth.ktor.infrastructure.adapter.output.exposed.configuration.initDatabaseConnection
import com.poisonedyouth.ktor.infrastructure.adapter.output.exposed.configuration.migrateDatabaseSchema
import com.poisonedyouth.ktor.infrastructure.server.configureSerialization
import com.poisonedyouth.ktor.infrastructure.server.setupDependencyInjection
import io.ktor.server.application.Application

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    setupDependencyInjection()
    initDatabaseConnection()
    migrateDatabaseSchema()
    configureThymeleaf()
    registerPageRoutes()
    registerPageOverviewRoutes()
}