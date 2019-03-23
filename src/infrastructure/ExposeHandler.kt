package com.sample.infrastructure

import org.jetbrains.exposed.sql.Database

class ExposeHandler {
    companion object {
        private const val DEFAULT_USER = "root" //"kotlin"
        private const val DEFAULT_PASSWORD = "secretpassword" //"kotlinpassword"
    }

    var user = DEFAULT_USER
    var password = DEFAULT_PASSWORD
    private val connection: Database

    init {
        connection = Database.connect("jdbc:mysql://localhost:3306/sample", driver = "com.mysql.jdbc.Driver",
            user = user, password = password)
    }

    fun getDBConnection(): Database{
        return connection
    }

}