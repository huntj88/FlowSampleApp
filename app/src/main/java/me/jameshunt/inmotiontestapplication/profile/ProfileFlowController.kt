package me.jameshunt.inmotiontestapplication.profile

import com.inmotionsoftware.promisekt.Promise
import com.inmotionsoftware.promisekt.map
import me.jameshunt.flow.generated.GeneratedProfileController
import me.jameshunt.flow.generated.GeneratedProfileController.ProfileFlowState.*

class ProfileFlowController : GeneratedProfileController() {

    override fun onGetProfile(state: GetProfile): Promise<FromGetProfile> {
        return Promise.value(Unit).map<Unit, FromGetProfile> {
            ProfileManager.profile
                ?.let { Done(it) }
                ?: ProfileRequest
        }
    }

    override fun onProfileRequest(state: ProfileRequest): Promise<FromProfileRequest> {
        // network request

        val networkResponse = Profile(
            email = "a@a.com",
            firstName = "wow",
            lastName = "last"
        )
        return state.toSaveProfile(networkResponse)
    }

    override fun onSaveProfile(state: SaveProfile): Promise<FromSaveProfile> {
        ProfileManager.profile = state.formData
        return Promise.value(Done(state.formData))
    }
}