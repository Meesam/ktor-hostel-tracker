package com.meesam.data.repositories

import com.meesam.data.db.DatabaseFactory.dbQuery
import com.meesam.data.tables.HostelPropertiesTable
import com.meesam.domain.dto.HostelPropertyResponse
import com.meesam.domain.dto.NewHostelPropertyRequest
import com.meesam.domain.dto.PagedResponse
import com.meesam.domain.dto.UpdateHostelPropertyRequest
import com.meesam.domain.exceptionHandler.DomainException
import com.meesam.domain.exceptionHandler.ResourceNotFoundException
import kotlinx.serialization.Contextual
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import java.util.UUID

class HostelPropertiesRepository : IHostelPropertiesRepository {
    companion object {
        private val logger = LoggerFactory.getLogger(HostelPropertiesRepository::class.java)
    }

    override suspend fun addNewProperty(newHostelPropertyRequest: NewHostelPropertyRequest): HostelPropertyResponse =
        dbQuery {
            try {
                val insertedPro = HostelPropertiesTable.insert {
                    it[hostelId] = newHostelPropertyRequest.hostelId
                    it[propertyName] = newHostelPropertyRequest.propertyName.trim()
                    it[propertyValue] = newHostelPropertyRequest.propertyValue.trim()
                }

                val propId = insertedPro.resultedValues?.singleOrNull()?.get(HostelPropertiesTable.id)
                    ?: throw DomainException("Failed to retrieve generated id for HostelPropertiesTable")

                HostelPropertyResponse(
                    propertyId = propId,
                    propertyName = newHostelPropertyRequest.propertyName,
                    propertyValue = newHostelPropertyRequest.propertyValue,
                    hostelId = newHostelPropertyRequest.hostelId,
                    isActive = true
                )
            } catch (e: ExposedSQLException) {
                throw DomainException(e.message.toString())
            } catch (e: Exception) {
                throw DomainException(e.message.toString())
            }
        }

    override suspend fun deleteProperty(propertyId: @Contextual UUID): Unit = dbQuery {
        try {
            getHostelProperty(propertyId)
            HostelPropertiesTable.update(where = { HostelPropertiesTable.isActive eq true and (HostelPropertiesTable.id eq propertyId) }) {
                it[isActive] = true
            }
        } catch (e: ResourceNotFoundException) {
            throw ResourceNotFoundException(e.message.toString())
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }

    override suspend fun getHostelProperty(propertyId: @Contextual UUID): HostelPropertyResponse = dbQuery {
        try {
            val row = HostelPropertiesTable.selectAll().where {
                HostelPropertiesTable.id eq propertyId and (HostelPropertiesTable.isActive eq true)

            }.limit(1)
                .singleOrNull()
                ?: throw ResourceNotFoundException("Property not found with Id '${propertyId}'")

            HostelPropertyResponse(
                propertyId = row[HostelPropertiesTable.id],
                hostelId = row[HostelPropertiesTable.hostelId],
                propertyValue = row[HostelPropertiesTable.propertyValue],
                propertyName = row[HostelPropertiesTable.propertyName],
                isActive = row[HostelPropertiesTable.isActive] == true
            )
        } catch (e: ResourceNotFoundException) {
            throw ResourceNotFoundException(e.message.toString())
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }

    override suspend fun updateHostelProperty(updateHostelPropertyRequest: UpdateHostelPropertyRequest): HostelPropertyResponse =
        dbQuery {
            try {
                val existingHostel = getHostelProperty(updateHostelPropertyRequest.propertyId)
                HostelPropertiesTable.update(where = { HostelPropertiesTable.id eq updateHostelPropertyRequest.propertyId }) {
                    it[propertyName] = updateHostelPropertyRequest.propertyName
                    it[propertyValue] = updateHostelPropertyRequest.propertyValue
                }
                HostelPropertyResponse(
                    propertyId = updateHostelPropertyRequest.propertyId,
                    hostelId = existingHostel.hostelId,
                    propertyValue = updateHostelPropertyRequest.propertyValue,
                    propertyName = updateHostelPropertyRequest.propertyName,
                    isActive = existingHostel.isActive
                )
            } catch (e: ExposedSQLException) {
                throw DomainException(e.message.toString())
            } catch (e: Exception) {
                throw DomainException(e.message.toString())
            }
        }

    private fun ResultRow.toHostelProperty(): HostelPropertyResponse = HostelPropertyResponse(
        propertyId = this[HostelPropertiesTable.id],
        hostelId = this[HostelPropertiesTable.hostelId],
        propertyName = this[HostelPropertiesTable.propertyName],
        propertyValue = this[HostelPropertiesTable.propertyValue],
        isActive = this[HostelPropertiesTable.isActive] == true
    )

    override suspend fun getAllHostelProperty(
        page: Int,
        size: Int,
        hostelId: @Contextual UUID
    ): PagedResponse<HostelPropertyResponse> = dbQuery {
        try {
            val totalItems = HostelPropertiesTable.selectAll().where {
                HostelPropertiesTable.isActive eq true and (HostelPropertiesTable.hostelId eq hostelId)
            }.count()
            val totalPages = (totalItems + size - 1) / size
            val data = HostelPropertiesTable.selectAll().where {
                HostelPropertiesTable.isActive eq true and (HostelPropertiesTable.hostelId eq hostelId)
            }
                .limit(size).offset(start = ((page - 1) * size).toLong())
                .map { it.toHostelProperty() }
            PagedResponse(data, page, totalPages, totalItems)
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }
}