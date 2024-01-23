package com.poisonedyouth.ktor.infrastructure.adapter.output.exposed.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.timestamp

object PageTable : UUIDTable("page") {
    val title = varchar("title", 255)
    val tag = varchar("tag", 255)
    val url = varchar("url", 255)
    val available = bool("available")
    val created = timestamp("created")
    val updated = timestamp("updated")
}