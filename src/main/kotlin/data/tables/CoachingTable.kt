package com.meesam.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID

object CoachingTable: Table(name = "coaching") {
    val id = uuid("id").clientDefault { UUID.randomUUID() }
    val name = varchar("name", 255)
    val contactName = varchar("contact_name", 50)
    val contactNumber = varchar("contact_number", 15)
    val address = varchar("address", 255)
    val city = varchar("city", 255).nullable()
    val state = varchar("state", 255).nullable()
    val country = varchar("country", 150).nullable()
    val zipCode = varchar("zip_code", 50).nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val isActive = bool("is_active").default(true).nullable()
    override val primaryKey = PrimaryKey(id)
}