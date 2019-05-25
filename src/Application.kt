package com.sample

import com.sample.model.User
import com.sample.model.UserRepository
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val userRepository = UserRepository()

    install(CORS) {
        anyHost()
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        post("/users") {
            val userJSON = call.receiveText()

            val mapper = jacksonObjectMapper()
            try {
                val user = mapper.readValue<User>(userJSON)
                if (userRepository.add(user)) {
                    call.respond(HttpStatusCode.OK, "")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "")
                }
            } catch (e: Exception){
                call.respond(HttpStatusCode.InternalServerError, "")
            }
        }

        get("/users") {
            try {
                val users = userRepository.findAll()
                val mapper = jacksonObjectMapper()
                val usersJSON = mapper.writeValueAsString(users)
                call.respond(HttpStatusCode.OK, usersJSON)
            } catch(e: Exception){
                call.respond(HttpStatusCode.InternalServerError, "")
            }
        }

        get("/user/{id}") {
            try {
                val user = call.parameters["id"]?.let {
                    userRepository.findById(it.toInt())
                }
                val mapper = jacksonObjectMapper()
                val userJSON = mapper.writeValueAsString(user)
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

