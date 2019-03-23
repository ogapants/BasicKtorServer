package com.sample.interfaces

import com.sample.domain.User
import com.sample.infrastructure.ExposeHandler
import com.sample.interfaces.controllers.UserController
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import java.lang.Exception

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val exposeHandler = ExposeHandler()
    val userController = UserController(exposeHandler.getDBConnection())
    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        post("/users") {
            val user = call.receiveText()

            if(userController.create(user)){
                call.respond(HttpStatusCode.OK,"")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "")
            }
        }

        get("/users") {
            try {
                val usersJSON = userController.findAll()
                call.respond(HttpStatusCode.OK, usersJSON)
            } catch(e: Exception){
                call.respond(HttpStatusCode.InternalServerError, "")
            }
        }

        get("/user/{id}") {
            try {
                val userJSON = call.parameters["id"]?.let {
                    userController.findById(it.toInt())
                }
                if(userJSON == null){
                    call.respond(HttpStatusCode.InternalServerError, "")
                } else {
                    call.respond(HttpStatusCode.OK, userJSON)
                }
            } catch(e: Exception){
                call.respond(HttpStatusCode.InternalServerError, "")
            }
        }
    }
}
