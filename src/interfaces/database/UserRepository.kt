package com.sample.interfaces.database

import com.sample.domain.User
import com.sample.usecases.UserRepositoryInterface
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


class UserRepository(private val connection: Database) : UserRepositoryInterface {

    override fun add(user: User): Boolean {
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
        } catch (se: SQLException){
            se.printStackTrace()
            false
        }
    }

    override fun findAll(): List<User> {
        return try {
            var userList = ArrayList<User>()

            transaction(connection) {
                Users.selectAll().forEach {
                    userList.add(User(it[Users.firstName], it[Users.lastName], it[Users.gender], it[Users.age]))
                }
            }
            userList
        } catch (se: SQLException){
            se.printStackTrace()
            throw se
        }
    }

    override fun findById(id: Int): User {
        return try {
            var user = User("", "", -1, -1)
            transaction(connection) {
                Users.select {
                    Users.id eq id
                }.forEach {
                    user = User(it[Users.firstName], it[Users.lastName], it[Users.gender], it[Users.age])
                }
            }
            user
        } catch (se: SQLException){
            se.printStackTrace()
            throw se
        }
    }
}
