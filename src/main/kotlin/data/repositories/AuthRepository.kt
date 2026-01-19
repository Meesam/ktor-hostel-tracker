package com.meesam.data.repositories

import com.meesam.data.db.DatabaseFactory.dbQuery
import com.meesam.data.tables.OtpTable
import com.meesam.data.tables.OtpTable.expiresAt
import com.meesam.data.tables.UserTable
import com.meesam.domain.dto.AuthenticationRequest
import com.meesam.domain.dto.AuthenticationResponse
import com.meesam.domain.dto.UserRequest
import com.meesam.domain.dto.UserResponse
import com.meesam.domain.exceptionHandler.ConflictException
import com.meesam.domain.exceptionHandler.DomainException
import com.meesam.domain.exceptionHandler.ResourceNotFoundException
import de.mkammerer.argon2.Argon2Factory
import io.ktor.server.plugins.NotFoundException
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.slf4j.LoggerFactory
import java.security.SecureRandom
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi

class AuthRepository : IAuthRepository {

    companion object {
        private val logger = LoggerFactory.getLogger(AuthRepository::class.java)
    }

    private val secureRandom = SecureRandom()

    private fun generateOtp(): Int {
        return secureRandom.nextInt(900_000) + 100_000
    }

    override suspend fun generateOtp(authenticationRequest: AuthenticationRequest): Int = dbQuery {
        try {
            val newOtp = generateOtp()
            val user = getUserByPhoneNumber(authenticationRequest.phoneNumber)
            OtpTable.insert {
                it[userId] = user.id
                it[phoneNumber] = authenticationRequest.phoneNumber
                it[otp] = newOtp
                it[expiresAt] = Clock.System.now() + 5.minutes
            }
            newOtp
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun register(userRequest: UserRequest): UserResponse = dbQuery {
        val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)

        val passwordValue =
            if (userRequest.password != null && userRequest.password != "") userRequest.password else userRequest.phone
        val passwordHash = argon2.hash(3, 1 shl 16, 1, passwordValue.toCharArray())
        try {
            val insertedUser = UserTable.insert {
                it[name] = userRequest.name
                it[email] = userRequest.email
                it[phoneNumber] = userRequest.phone
                it[password] = passwordHash
                it[role] = userRequest.role ?: "User"
                userRequest.dob?.let { v ->
                    it[dob] = v.toLocalDateTime(TimeZone.UTC).date
                }
            }
            val id = insertedUser.resultedValues?.singleOrNull()?.get(UserTable.id)
                ?: throw DomainException("Failed to retrieve generated id for user")

            UserResponse(
                id = id,
                name = userRequest.name,
                email = userRequest.email ?: "",
                role = userRequest.role ?: "User",
                phoneNumber = userRequest.phone
            )
        } catch (e: ExposedSQLException) {
            if (e.sqlState == "23505") {
                throw ConflictException("Phone number '${userRequest.phone}' already exists")
            }
            throw DomainException(e.message.toString())
        }
    }

    override suspend fun validateOtpAndLogin(otp: Int): UserResponse = dbQuery {
        try {
            val row = OtpTable
                .selectAll()
                .where{
                    OtpTable.otp eq otp and (expiresAt greaterEq Clock.System.now())
                }
                .singleOrNull()
                ?: throw ResourceNotFoundException("Invalid or expired Otp '${otp}'")
               getUserByPhoneNumber(row[OtpTable.phoneNumber])
        } catch (e: ResourceNotFoundException) {
            throw ResourceNotFoundException(e.message.toString())
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }

    override suspend fun getUserByPhoneNumber(phoneNumber: String): UserResponse = dbQuery {
        try {
            val row = UserTable
                .selectAll()
                .where { UserTable.phoneNumber eq phoneNumber.trim() and (UserTable.isActive eq true) }
                .limit(1)
                .singleOrNull()
                ?: throw ResourceNotFoundException("User not found with Phone number '${phoneNumber}'")
            UserResponse(
                id = row[UserTable.id],
                name = row[UserTable.name],
                email = row[UserTable.email],
                dob = row[UserTable.dob],
                lastLoginAt = row[UserTable.lastLoginAt],
                role = row[UserTable.role],
                profilePicUrl = row[UserTable.profilePicUrl],
                phoneNumber = row[UserTable.phoneNumber],
            )
        } catch (e: ResourceNotFoundException) {
            throw ResourceNotFoundException(e.message.toString())
        } catch (e: ExposedSQLException) {
            throw DomainException(e.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }

    override suspend fun deleteAllOtpWithPhoneNumber(phoneNumber: String): Unit = dbQuery {
        try {
            OtpTable.deleteWhere {
                OtpTable.phoneNumber eq phoneNumber.trim()
            }
        } catch (ex: ExposedSQLException) {
            throw DomainException(ex.message.toString())
        } catch (e: Exception) {
            throw DomainException(e.message.toString())
        }
    }
}