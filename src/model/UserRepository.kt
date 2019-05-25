package com.sample.model

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.SQLException

object Users : Table() {
    val id = integer("id").autoIncrement().primaryKey() // Column<String>
    val firstName = varchar("firstName", length = 50) // Column<String>
    val lastName = varchar("lastName", length = 50) // Column<String>
    val gender = integer("gender") // Column<Int>
    val age = integer("age") // Column<Int>
}

class User(val id: Int, val firstName: String, val lastName: String, val gender: Int, val age: Int)

class UserRepository {

    companion object {
        private const val DEFAULT_USER = "root"
        private const val DEFAULT_PASSWORD = "secretpassword"
    }

    var user = DEFAULT_USER
    var password = DEFAULT_PASSWORD
    private val connection: Database

    init {
        connection = Database.connect(
            "jdbc:mysql://172.28.0.2:3306/sample", driver = "com.mysql.jdbc.Driver",
            user = user, password = password
        )
    }

    fun add(user: User): Boolean {
        return try {
            transaction(connection) {
                SchemaUtils.create(Users)

                Users.insert {
                    it[firstName] = user.firstName
                    it[lastName] = user.lastName
                    it[gender] = user.gender
                    it[age] = user.age
                }
            }
            true
        } catch (se: SQLException) {
            se.printStackTrace()
            false
        }
    }

    fun findAll(): List<User> {
        return try {
            var userList = ArrayList<User>()

            transaction(connection) {
                Users.selectAll().forEach {
                    userList.add(
                        User(
                            it[Users.id],
                            it[Users.firstName],
                            it[Users.lastName],
                            it[Users.gender],
                            it[Users.age]
                        )
                    )
                }
            }
            userList
        } catch (se: SQLException) {
            se.printStackTrace()
            throw se
        }
    }

    fun findById(id: Int): User {
        return try {
            var user = User(-1, "", "", -1, -1)
            transaction(connection) {
                Users.select {
                    Users.id eq id
                }.forEach {
                    user = User(it[Users.id], it[Users.firstName], it[Users.lastName], it[Users.gender], it[Users.age])
                }
            }
            user
        } catch (se: SQLException) {
            se.printStackTrace()
            throw se
        }
    }
}