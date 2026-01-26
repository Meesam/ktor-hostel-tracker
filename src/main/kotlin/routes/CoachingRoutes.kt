package com.meesam.routes


import com.meesam.domain.dto.NewCoachingRequest
import com.meesam.domain.dto.UpdateCoachingRequest
import com.meesam.services.ICoachingService
import com.meesam.utils.BeanValidation
import com.meesam.utils.toUUIDOrNull
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.coachingRoutes(coachingService: ICoachingService){

    route("/admin/coaching"){
        route("/addNew"){
            post {
                val body = call.receive<NewCoachingRequest>()
                val errors = BeanValidation.errorsFor(body)
                if (errors.isNotEmpty()) {
                    call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to errors))
                    return@post
                }
                val result = coachingService.addNewCoaching(body)
                call.respond(HttpStatusCode.Created, message = result)
            }
        }

        route("/getCoaching"){
            get {
                val coachingId = call.request.queryParameters["id"].toUUIDOrNull()
                coachingId?.let {
                    val result = coachingService.getCoachingById(coachingId)
                    call.respond(HttpStatusCode.OK, result)
                } ?: call.respond(HttpStatusCode.BadRequest, "ID is missing or invalid")
            }
        }

        route("/deleteCoaching"){
            delete {
                val coachingId = call.request.queryParameters["id"].toUUIDOrNull()
                coachingId?.let {
                    val result = coachingService.delectCoaching(coachingId)
                    call.respond(HttpStatusCode.NoContent, result)
                } ?: call.respond(HttpStatusCode.BadRequest, "ID is missing or invalid")
            }
        }

        route("/updateCoaching"){
            post{
                val body = call.receive<UpdateCoachingRequest>()
                val errors = BeanValidation.errorsFor(body)
                if (errors.isNotEmpty()) {
                    call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to errors))
                    return@post
                }
                val result = coachingService.updateCoaching(body)
                call.respond(HttpStatusCode.OK, result)
            }
        }

        route("/getAllCoaching"){
            get {
                val page = call.request.queryParameters["page"]?.toInt() ?:-1
                val size = call.request.queryParameters["size"]?.toInt() ?:-1
                val result = coachingService.getAllCoaching(page = page, size = size)
                call.respond(HttpStatusCode.OK, result)
            }
        }
    }

}