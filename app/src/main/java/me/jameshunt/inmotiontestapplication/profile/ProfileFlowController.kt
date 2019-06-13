package me.jameshunt.inmotiontestapplication.profile

import com.inmotionsoftware.promisekt.Promise
import com.inmotionsoftware.promisekt.map
import me.jameshunt.flow.promise.DispatchExecutor
import me.jameshunt.flowcore.FlowController

class ProfileBusinessFlowController : FlowController<Unit, Profile>() {
    override fun onStart(state: InitialState<Unit>) {
        println("wow business logic")
        Promise.value(Unit).map(on = DispatchExecutor.background) {
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