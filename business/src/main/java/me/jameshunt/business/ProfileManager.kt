package me.jameshunt.business

import com.inmotionsoftware.promisekt.Promise

object ProfileManager {

    var profile: Profile? = null

    fun login(email: String, password: String): Promise<Boolean> {
        return when (email == "" && password == "") {
//        return when (email == "huntj88@gmail.com" && password == "qwert123") {
            true -> Promise.value(true)
            false -> Promise.value(false)
        }
    }
}

data class Profile(
    val email: String,
    val firstName: String,
    val lastName: String
)