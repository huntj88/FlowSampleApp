package me.jameshunt.business

import com.inmotionsoftware.promisekt.Promise
import me.jameshunt.flowcore.BusinessFlowController

class ProfileBusinessFlowController : BusinessFlowController<Unit, Profile>() {
    override fun onStart(state: InitialState<Unit>) {
        println("wow business logic")

        Promise.value(Unit).map {
            ProfileManager.profile
                ?.let { this.onDone(it) }
                ?: let {
                    Thread.sleep(5000)
                    val networkResponse = Profile(
                        email = "a@a.com",
                        firstName = "wow",
                        lastName = "last"
                    )
                    ProfileManager.profile = networkResponse
                    this.onDone(networkResponse)
                }
        }
    }
}