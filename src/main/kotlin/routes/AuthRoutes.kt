package com.meesam.routes

import com.meesam.domain.dto.AuthenticationRequest
import com.meesam.domain.dto.LoginRequest
import com.meesam.domain.dto.LoginResponse
import com.meesam.domain.dto.UserRequest
import com.meesam.domain.dto.UserUpdateRequest
import com.meesam.security.TokenService
import com.meesam.services.IAuthService
import com.meesam.utils.BeanValidation
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route


fun Route.authRoutes(authService: IAuthService) {
    val jwtIssuer = environment.config.property("jwt.issuer").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()
    val tokenService = TokenService(jwtIssuer, jwtAudience, jwtSecret)

    route("/auth") {
        route("/register") {
          post {
              val body = call.receive<UserRequest>()
              val errors = BeanValidation.errorsFor(body)
              if (errors.isNotEmpty()) {
                  call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to errors))
                  return@post
              }
              val result = authService.register(body)
              call.respond(HttpStatusCode.Created, message = result)
          }
        }

        route("/generate-otp") {
            post {
                val body = call.receive<AuthenticationRequest>()
                val errors = BeanValidation.errorsFor(body)
                if (errors.isNotEmpty()) {
                    call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to errors))
                    return@post
                }
                val result = authService.generateOtp(body)
                call.respond(HttpStatusCode.Created, message = result)
            }
        }

        route("/login"){
           post {
               val body = call.receive<LoginRequest>()
               val errors = BeanValidation.errorsFor(body)
               if (errors.isNotEmpty()) {
                   call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to errors))
                   return@post
               }
               val user = authService.validateOtpAndLogin(body.otp)
               user.let {
                   val access = tokenService.createAccessToken(user.phoneNumber!!, user.role)
                   val refresh = tokenService.createRefreshToken(user.id, phoneNumber = user.phoneNumber)
                   authService.saveRefreshToken(refresh.token, user.phoneNumber, user.id)
                   authService.updateUser(UserUpdateRequest(
                       id = user.id,
                       name = user.name,
                       email = user.email,
                   ))
                   call.respond(
                       HttpStatusCode.OK,
                       LoginResponse(
                           accessToken = access.token,
                           accessTokenExpiresAt = access.expiresAt.toString(),
                           refreshToken = refresh.token,
                           refreshTokenExpiresAt = refresh.expiresAt.toString(),
                           user = user
                       )
                   )
               }
           }
        }
    }
}