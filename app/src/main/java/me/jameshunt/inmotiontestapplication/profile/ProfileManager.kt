package me.jameshunt.inmotiontestapplication.profile

import me.jameshunt.flow.promise.Promise

object ProfileManager {

    var profile: Profile? = null

    fun login(email: String, password: String): Promise<Boolean> {
        return when (email == "huntj88@gmail.com" && password == "qwert123") {
            true -> Promise(true)
            false -> Promise(false)
        }
    }
}

data class Profile(
    val email: String,
    val firstName: String,
    val lastName: String
)