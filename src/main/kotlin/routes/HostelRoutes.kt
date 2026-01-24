package com.meesam.routes

import com.meesam.domain.dto.NewHostelRequest
import com.meesam.domain.dto.UpdateHostelRequest
import com.meesam.services.IHostelService
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
import org.apache.http.HttpStatus

fun Route.hostelRoutes(hostelService: IHostelService){
    route("/admin/hostel"){
        route("/addNew"){
            post {
                val body = call.receive<NewHostelRequest>()
                val errors = BeanValidation.errorsFor(body)
                if (errors.isNotEmpty()) {
                    call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to errors))
                    return@post
                }
                val result = hostelService.addNewHostel(body)
                call.respond(HttpStatusCode.Created, message = result)
            }
        }

        route("/getHostel"){
            get{
                val hostelId = call.request.queryParameters["id"].toUUIDOrNull()
                hostelId?.let {
                    val result = hostelService.getHostelById(hostelId)
                    call.respond(HttpStatusCode.OK, result)
                } ?: call.respond(HttpStatusCode.BadRequest, "ID is missing or invalid")
            }
        }

        route("/deleteHostel"){
            delete {
                val hostelId = call.request.queryParameters["id"].toUUIDOrNull()
                hostelId?.let {
                    val result = hostelService.delectHostel(hostelId)
                    call.respond(HttpStatusCode.NoContent, result)
                } ?: call.respond(HttpStatusCode.BadRequest, "ID is missing or invalid")
            }
        }

        route("/updateHostel"){
            post{
                val body = call.receive<UpdateHostelRequest>()
                val errors = BeanValidation.errorsFor(body)
                if (errors.isNotEmpty()) {
                    call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to errors))
                    return@post
                }
                val result = hostelService.updateHostel(body)
                call.respond(HttpStatusCode.OK, result)
            }
        }

        route("/getAllHostel"){
            get {
                val page = call.request.queryParameters["page"]?.toInt() ?:-1
                val size = call.request.queryParameters["size"]?.toInt() ?:-1
                val result = hostelService.getAllHostel(page, size)
                call.respond(HttpStatusCode.OK, result)
            }
        }
    }
}