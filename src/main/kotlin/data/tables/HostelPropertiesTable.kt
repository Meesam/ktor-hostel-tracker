package com.meesam.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID

object HostelPropertiesTable: Table(name = "hostel_properties") {
    val id = uuid("id").clientDefault { UUID.randomUUID() }
    val propertyName = varchar("property_name", 500)
    val propertyValue = varchar("property_value", 500)
    val hostelId = uuid("hostel_id").references(HostelTable.id)
    val isActive = bool("is_active").default(true).nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    override val primaryKey = PrimaryKey(id)
}