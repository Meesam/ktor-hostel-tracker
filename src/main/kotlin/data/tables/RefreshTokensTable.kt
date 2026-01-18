package com.meesam.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object RefreshTokensTable : Table("refresh_tokens") {
    val id = uuid("id").clientDefault { UUID.randomUUID() }
    val token = varchar("token", 128).index()
    val userId = uuid("user_id").references(UserTable.id).index()
    val phoneNumber = varchar("phoneNumber", 100).references(UserTable.phoneNumber).index()
    val expiresAt = timestamp("expires_at")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    override val primaryKey = PrimaryKey(id)
}