package me.jameshunt.inmotiontestapplication.profile

import me.jameshunt.flow.generated.GeneratedProfileController
import me.jameshunt.flow.generated.GeneratedProfileController.ProfileFlowState.*
import me.jameshunt.flow.promise.Promise
import me.jameshunt.flow.promise.then

class ProfileFlowController : GeneratedProfileController() {

    override fun onGetProfile(state: GetProfile): Promise<FromGetProfile> {
        return Promise(Unit).then<Unit, FromGetProfile> {
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
        return Promise(SaveProfile(networkResponse))
    }

    override fun onSaveProfile(state: SaveProfile): Promise<FromSaveProfile> {
        ProfileManager.profile = state.formData
        return Promise(Done(state.formData))
    }
}