package com.meesam.data.repositories

import com.meesam.data.db.DatabaseFactory.dbQuery
import com.meesam.data.tables.HostelTable
import com.meesam.data.tables.UserTable
import com.meesam.domain.dto.HostelResponse
import com.meesam.domain.dto.NewHostelRequest
import com.meesam.domain.dto.PagedResponse
import com.meesam.domain.dto.UpdateHostelRequest
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

class HostelRepository : IHostelRepository {
    companion object {
        private val logger = LoggerFactory.getLogger(HostelRepository::class.java)
    }

    override suspend fun addNewHostel(newHostelRequest: NewHostelRequest): HostelResponse = dbQuery {
        try {
            val insertedHostel = HostelTable.insert {
                it[name] = newHostelRequest.name.trim()
                it[address] = newHostelRequest.address.trim()
                it[city] = newHostelRequest.city.trim()
                it[state] = newHostelRequest.state.trim()
                it[country] = newHostelRequest.country.trim()
                it[zipCode] = newHostelRequest.zipCode.trim()
                it[contactName] = newHostelRequest.contactName.trim()
                it[contactNumber] = newHostelRequest.contactNumber.trim()
            }

            val hostelId = insertedHostel.resultedValues?.singleOrNull()?.get(HostelTable.id)
                ?: throw DomainException("Failed to retrieve generated id for HostelTable")

            HostelResponse(
                id = hostelId,
                name = newHostelRequest.name,
                address = newHostelRequest.address,
                city = newHostelRequest.city,
                state = newHostelRequest.state,
                country = newHostelRequest.country,
                zipCode = newHostelRequest.zipCode,
                contactName = newHostelRequest.contactName,
                contactNumber = newHostelRequest.contactNumber,
                isActive = true
            )
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }

    override suspend fun delectHostel(hostelId: @Contextual UUID): Unit = dbQuery {
        getHostelById(hostelId)
        HostelTable.update(where = { HostelTable.id eq hostelId }) {
            it[isActive] = false
        }
    }

    override suspend fun updateHostel(updateHostelRequest: UpdateHostelRequest): HostelResponse = dbQuery {
        try {
            val existingHostel = getHostelById(updateHostelRequest.hostelId)
            HostelTable.update(where = { HostelTable.id eq updateHostelRequest.hostelId }) {
                it[name] = updateHostelRequest.name ?: existingHostel.name
                it[address] = updateHostelRequest.address ?: existingHostel.address
                it[city] = updateHostelRequest.city ?: existingHostel.city
                it[state] = updateHostelRequest.state ?: existingHostel.state
                it[country] = updateHostelRequest.country ?: existingHostel.country
                it[zipCode] = updateHostelRequest.zipCode ?: existingHostel.zipCode
                it[contactName] = updateHostelRequest.contactName ?: existingHostel.contactName
                it[contactNumber] = updateHostelRequest.contactNumber ?: existingHostel.contactNumber
            }
            HostelResponse(
                id = updateHostelRequest.hostelId,
                name = updateHostelRequest.name ?: existingHostel.name,
                address = updateHostelRequest.address ?: existingHostel.address,
                city = updateHostelRequest.city ?: existingHostel.city,
                state = updateHostelRequest.state ?: existingHostel.state,
                country = updateHostelRequest.country ?: existingHostel.country,
                zipCode = updateHostelRequest.zipCode ?: existingHostel.zipCode,
                contactName = updateHostelRequest.contactName ?: existingHostel.contactName,
                contactNumber = updateHostelRequest.contactNumber ?: existingHostel.contactNumber,
                isActive = true
            )
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }

    override suspend fun getHostelById(hostelId: @Contextual UUID): HostelResponse = dbQuery {
        try {
            val row = HostelTable.selectAll()
                .where {
                    HostelTable.id eq hostelId and (HostelTable.isActive eq true)
                }
                .limit(1)
                .singleOrNull()
                ?: throw ResourceNotFoundException("Hostel not found with Id '${hostelId}'")

            HostelResponse(
                id = row[HostelTable.id],
                name = row[HostelTable.name],
                address = row[HostelTable.address],
                city = row[HostelTable.city],
                state = row[HostelTable.state],
                country = row[HostelTable.country],
                zipCode = row[HostelTable.zipCode],
                contactName = row[HostelTable.contactName],
                contactNumber = row[HostelTable.contactNumber],
                isActive = row[HostelTable.isActive] == true
            )
        } catch (e: ResourceNotFoundException) {
            throw ResourceNotFoundException(e.message.toString())
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }

    private fun ResultRow.toHostel(): HostelResponse = HostelResponse(
        id = this[HostelTable.id],
        name = this[HostelTable.name],
        address = this[HostelTable.address],
        city = this[HostelTable.city],
        state = this[HostelTable.state],
        country = this[HostelTable.country],
        zipCode = this[HostelTable.zipCode],
        contactName = this[HostelTable.contactName],
        contactNumber = this[HostelTable.contactNumber],
        isActive = this[HostelTable.isActive] == true
    )

    override suspend fun getAllHostel(
        page: Int,
        size: Int
    ): PagedResponse<HostelResponse> = dbQuery {
        try {
            val totalItems = HostelTable.selectAll().where {
                HostelTable.isActive eq true
            }.count()
            val totalPages = (totalItems + size - 1) / size
            val data = HostelTable.selectAll().where {
                HostelTable.isActive eq true
            }
            .limit(size).offset(start = ((page - 1) * size).toLong())
            .map { it.toHostel() }
            PagedResponse(data, page, totalPages, totalItems)
        }catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }
}