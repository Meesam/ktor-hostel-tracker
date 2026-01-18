package com.meesam.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID

object UserTable : Table("users") {
    val id = uuid("id").clientDefault { UUID.randomUUID() }
    val name = varchar("name", 100)
    val email = varchar("email", 100).nullable()
    val phoneNumber = varchar("phoneNumber", 15).uniqueIndex("uq_users_phone")
    val dob = date("dob").nullable()
    val lastLoginAt = datetime("last_login_at").nullable()
    val profilePicUrl = varchar("profile_pic_url", 255).nullable()
    val password = varchar("password", 255)
    val role = varchar("role", 20)
    val isActive = bool("is_active").default(true).nullable()
    val isActivatedByOtp = bool("is_activated_by_otp").default(false).nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    override val primaryKey = PrimaryKey(id)
}