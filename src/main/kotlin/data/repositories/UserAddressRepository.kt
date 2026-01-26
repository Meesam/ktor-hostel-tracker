package com.meesam.data.repositories

import com.meesam.data.db.DatabaseFactory.dbQuery
import com.meesam.data.tables.HostelPropertiesTable
import com.meesam.data.tables.UserAddressTable
import com.meesam.data.tables.UserTable
import com.meesam.domain.dto.CoachingResponse
import com.meesam.domain.dto.HostelPropertyResponse
import com.meesam.domain.dto.PagedResponse
import com.meesam.domain.dto.UserAddressRequest
import com.meesam.domain.dto.UserAddressResponse
import com.meesam.domain.dto.UserAddressUpdateRequest
import com.meesam.domain.exceptionHandler.DomainException
import com.meesam.domain.exceptionHandler.ResourceNotFoundException
import kotlinx.serialization.Contextual
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.util.UUID

class UserAddressRepository: IUserAddressRepository {

   private suspend fun isUserExists(userId: @Contextual UUID): Boolean = dbQuery {
        try {
            userId.let {
                UserTable.selectAll().where {
                    UserTable.id eq userId and (UserTable.isActive eq true)
                }.singleOrNull()?.let { true } ?: false
            } ?: false

        } catch (ex: ResourceNotFoundException) {
            throw ResourceNotFoundException(ex.message.toString())
        } catch (ex: ExposedSQLException) {
            throw DomainException(ex.message.toString())
        } catch (ex: Exception) {
            throw DomainException(ex.message.toString())
        }
    }

    override suspend fun addNewAddress(userAddressRequest: UserAddressRequest): UserAddressResponse = dbQuery {
        try {
            val isUserExistedDB = isUserExists(userAddressRequest.userId)
            if (!isUserExistedDB) {
                throw ResourceNotFoundException("User not found")
            }
         val insertedUserAddress =   UserAddressTable.insert {
                it[userId] = userAddressRequest.userId
                it[address] = userAddressRequest.address.trim()
                it[city] = userAddressRequest.city.trim()
                it[state] = userAddressRequest.state.trim()
                it[country] = userAddressRequest.country.trim()
                it[zipCode] = userAddressRequest.zipCode.trim()
                it[contactName] = userAddressRequest.contactName.trim()
                it[contactNumber] = userAddressRequest.contactNumber.trim()
            }
            val userAddressId = insertedUserAddress.resultedValues?.singleOrNull()?.get(UserAddressTable.id)
                ?: throw DomainException("Failed to retrieve generated id for UserAddressTable")
            UserAddressResponse(
                id = userAddressId,
                address = userAddressRequest.address.trim(),
                city = userAddressRequest.city.trim(),
                state = userAddressRequest.state.trim(),
                country = userAddressRequest.country.trim(),
                zipCode = userAddressRequest.zipCode.trim(),
                contactName = userAddressRequest.contactName.trim(),
                contactNumber = userAddressRequest.contactNumber.trim(),
                userId = userAddressRequest.userId
            )
        } catch (ex: ResourceNotFoundException) {
            throw ResourceNotFoundException(ex.message.toString())
        } catch (ex: ExposedSQLException) {
            throw DomainException(ex.message.toString())
        } catch (ex: Exception) {
            throw DomainException(ex.message.toString())
        }
    }

    override suspend fun updateAddress(updateRequest: UserAddressUpdateRequest): UserAddressResponse = dbQuery {
        try {
            val isUserExistedDB = isUserExists(updateRequest.userId)
            if (!isUserExistedDB) {
                throw ResourceNotFoundException("User not found")
            }
            UserAddressTable.update(where = { UserAddressTable.id eq updateRequest.addressId}) {
                it[userId] = updateRequest.userId
                it[address] = updateRequest.address.trim()
                it[city] = updateRequest.city.trim()
                it[state] = updateRequest.state.trim()
                it[country] = updateRequest.country.trim()
                it[zipCode] = updateRequest.zipCode.trim()
                it[contactName] = updateRequest.contactName.trim()
                it[contactNumber] = updateRequest.contactNumber.trim()
            }
            UserAddressResponse(
                id = updateRequest.addressId,
                address = updateRequest.address.trim(),
                city = updateRequest.city.trim(),
                state = updateRequest.state.trim(),
                country = updateRequest.country.trim(),
                zipCode = updateRequest.zipCode.trim(),
                contactName = updateRequest.contactName.trim(),
                contactNumber = updateRequest.contactNumber.trim(),
                userId = updateRequest.userId
            )
        } catch (ex: ResourceNotFoundException) {
            throw ResourceNotFoundException(ex.message.toString())
        } catch (ex: ExposedSQLException) {
            throw DomainException(ex.message.toString())
        } catch (ex: Exception) {
            throw DomainException(ex.message.toString())
        }
    }

    override suspend fun deleteAddress(addressId: @Contextual UUID):Unit = dbQuery {
        try {
            UserAddressTable.select(UserAddressTable.id).where { UserAddressTable.id eq addressId }.singleOrNull()
                ?: throw ResourceNotFoundException("Address not found")
            UserAddressTable.update(where = {UserAddressTable.id eq addressId}) {
                it[isActive] = false
            }
        } catch (ex: ResourceNotFoundException) {
            throw ResourceNotFoundException(ex.message.toString())
        } catch (ex: ExposedSQLException) {
            throw DomainException(ex.message.toString())
        } catch (ex: Exception) {
            throw DomainException(ex.message.toString())
        }
    }

    override suspend fun getAddressById(addressId: @Contextual UUID): UserAddressResponse = dbQuery {
        try {
            val row = UserAddressTable.selectAll()
                .where {
                    UserAddressTable.id eq addressId
                }
                .limit(1)
                .singleOrNull()
                ?:throw ResourceNotFoundException("Address not found with Id $addressId")
            UserAddressResponse(
                id = row[UserAddressTable.id],
                address = row[UserAddressTable.address],
                city = row[UserAddressTable.city],
                state = row[UserAddressTable.state],
                country = row[UserAddressTable.country],
                zipCode = row[UserAddressTable.zipCode],
                contactName = row[UserAddressTable.contactName],
                contactNumber = row[UserAddressTable.contactNumber],
                userId = row[UserAddressTable.userId]
            )
        }catch (ex: ResourceNotFoundException) {
            throw ResourceNotFoundException(ex.message.toString())
        } catch (ex: ExposedSQLException) {
            throw DomainException(ex.message.toString())
        } catch (ex: Exception) {
            throw DomainException(ex.message.toString())
        }
    }

    private fun ResultRow.toUserAddress(): UserAddressResponse = UserAddressResponse(
        id = this[UserAddressTable.id],
        userId = this[UserAddressTable.userId],
        address = this[UserAddressTable.address],
        state = this[UserAddressTable.state],
        city = this[UserAddressTable.city],
        country = this[UserAddressTable.country],
        zipCode = this[UserAddressTable.zipCode],
        contactNumber = this[UserAddressTable.contactNumber],
        contactName = this[UserAddressTable.contactName]
    )

    override suspend fun getAllAddressByUser(
        userId: @Contextual UUID,
        page: Int,
        size: Int
    ): PagedResponse<UserAddressResponse> = dbQuery {
        try {
            val totalItems = UserAddressTable.selectAll().where {
                UserAddressTable.isActive eq true and (UserAddressTable.userId eq userId)
            }.count()
            val totalPages = (totalItems + size - 1) / size
            val data = UserAddressTable.selectAll().where {
                UserAddressTable.isActive eq true and (UserAddressTable.userId eq userId)
            }
                .limit(size).offset(start = ((page - 1) * size).toLong())
                .map { it.toUserAddress() }
            PagedResponse(data, page, totalPages, totalItems)
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }
}