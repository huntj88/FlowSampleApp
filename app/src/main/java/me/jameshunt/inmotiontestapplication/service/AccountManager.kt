package me.jameshunt.inmotiontestapplication.service

import me.jameshunt.flow.promise.Promise

object AccountManager {

    var currentUser: User? = null

    fun login(email: String, password: String): Promise<Boolean> {
        return when (email == "huntj88@gmail.com" && password == "qwert123") {
            true -> {
                currentUser = User(email, "james", "hunt")
                Promise(true)
            }
            false -> Promise(false)
        }
    }
}

data class User(
    val email: String,
    val firstName: String,
    val lastName: String
)