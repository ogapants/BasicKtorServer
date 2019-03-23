package com.sample.interfaces.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.sample.domain.User
import com.sample.interfaces.database.UserRepository
import com.sample.usecases.UserInteractor
import org.jetbrains.exposed.sql.Database

class UserController(database: Database) {
    private val userInteractor = UserInteractor( UserRepository(database))

    fun create(userJSON: String): Boolean {
        val mapper = jacksonObjectMapper()
        return try {
            val user = mapper.readValue<User>(userJSON)

            userInteractor.add(user)
        } catch (e: Exception){
            throw e
        }
    }

    fun findById(id: Int): String {
        return try {
            val user = userInteractor.findById(id)

            val mapper = jacksonObjectMapper()
            mapper.writeValueAsString(user)
        } catch (e: Exception){
            throw e
        }
    }

    fun findAll(): String {
        return try {
            val userArray = userInteractor.findAll()

            val mapper = jacksonObjectMapper()
            mapper.writeValueAsString(userArray)
        } catch (e: Exception){
            throw e
        }
    }
}