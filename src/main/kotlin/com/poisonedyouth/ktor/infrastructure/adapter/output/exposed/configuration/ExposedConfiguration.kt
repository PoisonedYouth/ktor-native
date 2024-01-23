package com.poisonedyouth.ktor.infrastructure.adapter.output.exposed.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.util.IsolationLevel
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger(Application::class.java)

fun Application.initDatabaseConnection() {
    logger.info("Start initializing database connection.")
    val config = HikariConfig()
    config.driverClassName = environment.config.property("database.driver-classname").getString()
    config.jdbcUrl = environment.config.property("database.jdbcUrl").getString()
    config.username = environment.config.property("database.username").getString()
    config.password = environment.config.property("database.password").getString()
    config.maximumPoolSize = 10
    config.isAutoCommit = true
    config.transactionIsolation = IsolationLevel.TRANSACTION_REPEATABLE_READ.name
    config.validate()
    val datasource = HikariDataSource(config)
    Database.connect(datasource)
    logger.info("Connected database with datasource '$datasource'.")
}