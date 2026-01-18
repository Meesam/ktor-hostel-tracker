package com.meesam.data.db

import com.meesam.data.tables.CoachingTable
import com.meesam.data.tables.HostelImagesTable
import com.meesam.data.tables.HostelPropertiesTable
import com.meesam.data.tables.HostelTable
import com.meesam.data.tables.OtpTable
import com.meesam.data.tables.RefreshTokensTable
import com.meesam.data.tables.UserAddressTable
import com.meesam.data.tables.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.ApplicationEnvironment
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import java.sql.Connection

object DatabaseFactory {
    fun init(environment: ApplicationEnvironment) {
        val config = hikari(environment)
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
        // Optional: set default isolation
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        // Dev-time auto schema sync (use Flyway in production)
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                UserTable,
                HostelTable,
                HostelImagesTable,
                HostelPropertiesTable,
                CoachingTable,
                UserAddressTable,
                RefreshTokensTable,
                OtpTable
            )
        }
    }

    private fun hikari(environment: ApplicationEnvironment): HikariConfig {
        val cfg = HikariConfig().apply {
            jdbcUrl = environment.config.propertyOrNull("db.jdbcUrl")?.getString()
                ?: "jdbc:postgresql://localhost:5432/ktor_hostel_db"
            driverClassName = environment.config.property("db.driver").getString()
            username = environment.config.propertyOrNull("db.user")?.getString() ?: "postgres"
            password = environment.config.propertyOrNull("db.password")?.getString() ?: "admin"
            maximumPoolSize = environment.config.propertyOrNull("db.maximumPoolSize")?.getString()?.toInt() ?: 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return cfg
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) {
            addLogger(StdOutSqlLogger)
            block()
        }

}