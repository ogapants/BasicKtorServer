package com.sample.usecases

import com.sample.domain.User

interface UserRepositoryInterface {
    fun add(user: User): Boolean
    fun findAll(): List<User>
    fun findById(id: Int): User
}