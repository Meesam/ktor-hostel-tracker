package com.meesam.routes

import com.meesam.domain.dto.UserAddressRequest
import com.meesam.domain.dto.UserAddressUpdateRequest
import com.meesam.services.IUserAddressService
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

fun Route.userAddressRoute(userAddressService: IUserAddressService){

   route("/address"){
       route("/addAddress"){
           post {
               val body = call.receive<UserAddressRequest>()
               val errors = BeanValidation.errorsFor(body)
               if (errors.isNotEmpty()) {
                   call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to errors))
                   return@post
               }
              val result = userAddressService.addNewAddress(body)
               call.respond(HttpStatusCode.Created, result)
           }
       }

       route("/deleteAddress"){
           delete {
               val addressId = call.request.queryParameters["id"].toUUIDOrNull()
               addressId?.let {
                   userAddressService.deleteAddress(addressId)
                   call.respond(HttpStatusCode.NoContent)
               } ?: call.respond(HttpStatusCode.BadRequest, "ID is missing or invalid")
           }
       }

       route("/getAllAddress"){
           get {
               val userId = call.request.queryParameters["userId"].toUUIDOrNull()
               userId?.let {
                   val page = call.request.queryParameters["page"]?.toInt() ?:-1
                   val size = call.request.queryParameters["size"]?.toInt() ?:-1
                   val result = userAddressService.getAllAddressByUser(userId = userId, page = page, size = size)
                   call.respond(HttpStatusCode.OK, result)
               }?: call.respond(HttpStatusCode.BadRequest, "ID is missing or invalid")
           }
       }

       route("/updateAddress"){
           post {
               val body = call.receive<UserAddressUpdateRequest>()
               val errors = BeanValidation.errorsFor(body)
               if (errors.isNotEmpty()) {
                   call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to errors))
                   return@post
               }
              val result = userAddressService.updateAddress(body)
               call.respond(HttpStatusCode.OK, result)
           }
       }

       route("/getAddress"){
           get {
               val addressId = call.request.queryParameters["id"].toUUIDOrNull()
               addressId?.let {
                   val result = userAddressService.getAddressById(addressId)
                   call.respond(HttpStatusCode.OK, result)
               }?: call.respond(HttpStatusCode.BadRequest, "ID is missing or invalid")
           }
       }
   }

}