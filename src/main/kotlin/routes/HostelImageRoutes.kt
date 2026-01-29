package com.meesam.routes


import com.meesam.services.IHostelImageService
import com.meesam.utils.toUUIDOrNull
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.host
import io.ktor.server.routing.port
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Contextual
import java.io.File
import java.util.UUID

fun Route.hostelImageRoute(hostelImageService: IHostelImageService){
    val config = environment.config
    val host = config.property("ktor.deployment.host").getString()
    val port = config.property("ktor.deployment.port").getString()

  route("/hostelImages"){
      route("/uploadHostelImage") {
          post {
              val multipart = call.receiveMultipart()
              var hostelId: @Contextual UUID? = null
              var fileUrl: String? = null

              multipart.forEachPart { part ->
                  when (part) {
                      is PartData.FormItem -> {
                          val valueType = part.name.toString()
                          when (valueType) {
                              "hostelId" -> {
                                  hostelId = part.value.toUUIDOrNull()!!
                              }
                          }
                      }

                      is PartData.FileItem -> {
                          val fileExtension = part.originalFileName?.takeLastWhile { it != '.' }
                          val fileName = UUID.randomUUID().toString() + "." + fileExtension // Generate unique name
                          val uploadDir = File("uploads/images")
                          uploadDir.mkdirs()
                          val file = File(uploadDir, fileName) // Save to a public directory
                          // Write the bytes to the file
                          part.streamProvider().use { inputStream ->
                              file.outputStream().buffered().use { outputStream ->
                                  inputStream.copyTo(outputStream)
                              }
                          }
                          // Construct the public URL
                          fileUrl = "$host:$port/images/$fileName"
                      }

                      else -> {}
                  }
                  if (hostelId != null && fileUrl != null) {
                      hostelImageService.addHostelImage(hostelId = hostelId, imagePath = fileUrl)
                  }
                  part.dispose()
              }
              call.respond(HttpStatusCode.OK)
          }
      }
  }
}