package me.jameshunt.business

import kotlinx.coroutines.delay

object ProfileManager {

    var profile: Profile? = null

    suspend fun login(email: String, password: String): Boolean {
        delay(1000)
        return (email == "" && password == "")
    }
}

data class Profile(
    val email: String,
    val firstName: String,
    val lastName: String
)