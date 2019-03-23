package com.sample.usecases

import com.sample.domain.User

class UserInteractor(private val userRepository: UserRepositoryInterface) {
    fun add(user: User ): Boolean{
        return userRepository.add(user)
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun findById(id: Int): User {
        return userRepository.findById(id)
    }
}