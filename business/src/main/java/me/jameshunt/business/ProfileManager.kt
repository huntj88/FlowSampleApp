package me.jameshunt.business

import com.inmotionsoftware.promisekt.Promise

object ProfileManager {

    var profile: Profile? = null

    suspend fun login(email: String, password: String): Boolean {
        return email == "" && password == ""
    }
}

data class Profile(
    val email: String,
    val firstName: String,
    val lastName: String
)