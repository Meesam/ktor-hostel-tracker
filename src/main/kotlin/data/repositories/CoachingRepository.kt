package com.meesam.data.repositories

import com.meesam.data.db.DatabaseFactory.dbQuery
import com.meesam.data.tables.CoachingTable
import com.meesam.domain.dto.CoachingResponse
import com.meesam.domain.dto.NewCoachingRequest
import com.meesam.domain.dto.PagedResponse
import com.meesam.domain.dto.UpdateCoachingRequest
import com.meesam.domain.exceptionHandler.DomainException
import com.meesam.domain.exceptionHandler.ResourceNotFoundException
import kotlinx.serialization.Contextual
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import java.util.UUID

class CoachingRepository : ICoachingRepository {
    companion object {
        private val logger = LoggerFactory.getLogger(CoachingRepository::class.java)
    }

    override suspend fun addNewCoaching(newCoachingRequest: NewCoachingRequest): CoachingResponse = dbQuery {
        try {
            val insertedCoaching = CoachingTable.insert {
                it[CoachingTable.name] = newCoachingRequest.name.trim()
                it[CoachingTable.address] = newCoachingRequest.address.trim()
                it[CoachingTable.city] = newCoachingRequest.city.trim()
                it[CoachingTable.state] = newCoachingRequest.state.trim()
                it[CoachingTable.country] = newCoachingRequest.country.trim()
                it[CoachingTable.zipCode] = newCoachingRequest.zipCode.trim()
                it[CoachingTable.contactName] = newCoachingRequest.contactName.trim()
                it[CoachingTable.contactNumber] = newCoachingRequest.contactNumber.trim()
            }
            val coachingId = insertedCoaching.resultedValues?.singleOrNull()?.get(CoachingTable.id)
                ?: throw DomainException("Failed to retrieve generated id for CoachingTable")
            CoachingResponse(
                coachingId = coachingId,
                name = newCoachingRequest.name,
                address = newCoachingRequest.address,
                city = newCoachingRequest.city,
                state = newCoachingRequest.state,
                country = newCoachingRequest.country,
                zipCode = newCoachingRequest.zipCode,
                contactName = newCoachingRequest.contactName,
                contactNumber = newCoachingRequest.contactNumber,
                isActive = true
            )
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }

    override suspend fun delectCoaching(coachingId: @Contextual UUID): Unit = dbQuery {
        getCoachingById(coachingId)
        CoachingTable.update(where = { CoachingTable.id eq coachingId }) {
            it[isActive] = false
        }
    }

    override suspend fun updateCoaching(updateCoachingRequest: UpdateCoachingRequest): CoachingResponse = dbQuery {
        try {
            val existingCoaching = getCoachingById(updateCoachingRequest.coachingId)
            CoachingTable.update(where = { CoachingTable.id eq updateCoachingRequest.coachingId }) {
                it[name] = updateCoachingRequest.name
                it[address] = updateCoachingRequest.address
                it[city] = updateCoachingRequest.city
                it[state] = updateCoachingRequest.state
                it[country] = updateCoachingRequest.country
                it[zipCode] = updateCoachingRequest.zipCode
                it[contactName] = updateCoachingRequest.contactName
                it[contactNumber] = updateCoachingRequest.contactNumber
            }
            CoachingResponse(
                coachingId = updateCoachingRequest.coachingId,
                name = updateCoachingRequest.name,
                address = updateCoachingRequest.address,
                city = updateCoachingRequest.city,
                state = updateCoachingRequest.state,
                country = updateCoachingRequest.country,
                zipCode = updateCoachingRequest.zipCode,
                contactName = updateCoachingRequest.contactName,
                contactNumber = updateCoachingRequest.contactNumber,
                isActive = true
            )
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }

    override suspend fun getCoachingById(coachingId: @Contextual UUID): CoachingResponse = dbQuery {
        try {
            val row = CoachingTable.selectAll()
                .where {
                    CoachingTable.id eq coachingId and (CoachingTable.isActive eq true)
                }
                .limit(1)
                .singleOrNull()
                ?: throw ResourceNotFoundException("Coaching not found with Id '${coachingId}'")

            CoachingResponse(
                coachingId = row[CoachingTable.id],
                name = row[CoachingTable.name],
                address = row[CoachingTable.address],
                city = row[CoachingTable.city] ?: "",
                state = row[CoachingTable.state] ?: "",
                country = row[CoachingTable.country] ?: "",
                zipCode = row[CoachingTable.zipCode] ?: "",
                contactName = row[CoachingTable.contactName],
                contactNumber = row[CoachingTable.contactNumber],
                isActive = row[CoachingTable.isActive] == true
            )
        } catch (e: ResourceNotFoundException) {
            throw ResourceNotFoundException(e.message.toString())
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }

    private fun ResultRow.toCoaching(): CoachingResponse = CoachingResponse(
        coachingId = this[CoachingTable.id],
        name = this[CoachingTable.name],
        address = this[CoachingTable.address],
        city = this[CoachingTable.city] ?: "",
        state = this[CoachingTable.state] ?: "",
        country = this[CoachingTable.country] ?: "",
        zipCode = this[CoachingTable.zipCode] ?: "",
        contactName = this[CoachingTable.contactName],
        contactNumber = this[CoachingTable.contactNumber],
        isActive = this[CoachingTable.isActive] == true
    )

    override suspend fun getAllCoaching(
        page: Int,
        size: Int
    ): PagedResponse<CoachingResponse> = dbQuery {
        try {
            val totalItems = CoachingTable.selectAll().where {
                CoachingTable.isActive eq true
            }.count()
            val totalPages = (totalItems + size - 1) / size
            val data = CoachingTable.selectAll().where {
                CoachingTable.isActive eq true
            }
                .limit(size).offset(start = ((page - 1) * size).toLong())
                .map { it.toCoaching() }
            PagedResponse(data, page, totalPages, totalItems)
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }
}