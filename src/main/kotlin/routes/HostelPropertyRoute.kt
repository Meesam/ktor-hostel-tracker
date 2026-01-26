package com.meesam.routes

import com.meesam.domain.dto.NewHostelPropertyRequest
import com.meesam.domain.dto.UpdateHostelPropertyRequest
import com.meesam.services.IHostelPropertyService
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

fun Route.hostelPropertyRoutes(hostelPropertyService: IHostelPropertyService){
    route("/admin/hostelProperties"){
        route("/addNew"){
            post {
                val body = call.receive<NewHostelPropertyRequest>()
                val errors = BeanValidation.errorsFor(body)
                if (errors.isNotEmpty()) {
                    call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to errors))
                    return@post
                }
                val result = hostelPropertyService.addNewProperty(body)
                call.respond(HttpStatusCode.Created, message = result)
            }
        }

        route("/update"){
            post {
                val body = call.receive<UpdateHostelPropertyRequest>()
                val errors = BeanValidation.errorsFor(body)
                if (errors.isNotEmpty()) {
                    call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to errors))
                    return@post
                }
                val result = hostelPropertyService.updateHostelProperty(body)
                call.respond(HttpStatusCode.OK, message = result)
            }
        }
        route("/getHostelPropertyById"){
            get{
                val properTyId = call.request.queryParameters["id"].toUUIDOrNull()
                properTyId?.let {
                    val result = hostelPropertyService.getHostelProperty(properTyId)
                    call.respond(HttpStatusCode.OK, result)
                } ?: call.respond(HttpStatusCode.BadRequest, "ID is missing or invalid")
            }
        }

        route("/getAllHostelProperty"){
            get{
                val hostelId = call.request.queryParameters["hostelId"].toUUIDOrNull()
                val page = call.request.queryParameters["page"]?.toInt() ?:-1
                val size = call.request.queryParameters["size"]?.toInt() ?:-1
                hostelId?.let {
                    val result = hostelPropertyService.getAllHostelProperty(page = page, size = size, hostelId = hostelId)
                    call.respond(HttpStatusCode.OK, result)
                } ?: call.respond(HttpStatusCode.BadRequest, "ID is missing or invalid")
            }
        }

        route("/deleteProperty"){
            delete {
                val propertyId = call.request.queryParameters["id"].toUUIDOrNull()
                propertyId?.let {
                    val result = hostelPropertyService.deleteProperty(propertyId)
                    call.respond(HttpStatusCode.NoContent, result)
                } ?: call.respond(HttpStatusCode.BadRequest, "ID is missing or invalid")
            }
        }
    }

}