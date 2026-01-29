package com.meesam.plugins

import com.meesam.routes.authRoutes
import com.meesam.routes.coachingRoutes
import com.meesam.routes.hostelImageRoute
import com.meesam.routes.hostelPropertyRoutes
import com.meesam.routes.hostelRoutes
import com.meesam.routes.userAddressRoute
import com.meesam.services.IAuthService
import com.meesam.services.ICoachingService
import com.meesam.services.IHostelImageService
import com.meesam.services.IHostelPropertyService
import com.meesam.services.IHostelService
import com.meesam.services.IUserAddressService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.http.content.staticFiles
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.*
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject
import java.io.File

fun Application.configureRouting() {
    val authService by inject<IAuthService>()
    val hostelService by inject<IHostelService>()
    val hostelPropertyService by inject<IHostelPropertyService>()
    val coachingService by inject<ICoachingService>()
    val userAddressService by inject<IUserAddressService>()
    val hostelImageService by inject<IHostelImageService>()
    routing {
        route("/api/v1") {
            get("/health-check") {
                call.respondText("Server is running, ready to handle requests")
            }
            authRoutes(authService)
            hostelRoutes(hostelService)
            hostelPropertyRoutes(hostelPropertyService)
            coachingRoutes(coachingService)
            userAddressRoute(userAddressService)
            hostelImageRoute(hostelImageService)
            authenticate("auth-jwt") {
               // categoryRoutes()
                //userRoutes()
                //attributeRoutes()
               // productRoutes()
                //productAttributeRoutes()
                //userCartRoutes()
            }
        }

        staticFiles("/images", File("uploads/images")) {
            // Optional: you can configure caching, etc. here
        }
    }
}
