package com.poisonedyouth.ktor.infrastructure.adapter.output.exposed.configuration

import io.ktor.server.application.Application
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger(Application::class.java)

fun Application.migrateDatabaseSchema() {
    logger.info("Starting database schema migration...")

    val flyway = FluentConfiguration(Application::class.java.classLoader)
        .validateOnMigrate(true)
        .validateMigrationNaming(true)
        .dataSource(
            environment.config.property("database.jdbcUrl").getString(),
            environment.config.property("database.username").getString(),
            environment.config.property("database.password").getString()
        )
        .load()
    flyway.migrate()
    logger.info("Finished database schema migration.")
}