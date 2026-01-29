package com.meesam.data.repositories

import com.meesam.data.db.DatabaseFactory.dbQuery
import com.meesam.data.tables.HostelImagesTable
import com.meesam.data.tables.HostelTable
import com.meesam.domain.dto.HostelResponse
import com.meesam.domain.exceptionHandler.DomainException
import com.meesam.domain.exceptionHandler.ResourceNotFoundException
import kotlinx.serialization.Contextual
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class HostelImageRepository: IHostelImageRepository {

     suspend fun getHostelById(hostelId: @Contextual UUID): HostelResponse = dbQuery {
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

    override suspend fun addHostelImage(
        hostelIdValue: @Contextual UUID,
        imagePath: String
    ): Unit = dbQuery {
        try {
            getHostelById(hostelIdValue)
            HostelImagesTable.insert {
                it[hostelId] = hostelIdValue
                it[imageUrl] = imagePath
            }

        }catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }

    override suspend fun getAllHostelImages(hostelIdValue: @Contextual UUID): List<String> = dbQuery {
        try {
            getHostelById(hostelIdValue)
             HostelImagesTable.selectAll()
                .where{
                    HostelImagesTable.hostelId eq hostelIdValue and (HostelImagesTable.isActive eq true)
                }.map { it[HostelImagesTable.imageUrl] }
        }catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }
}