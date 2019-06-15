package me.jameshunt.business

import com.inmotionsoftware.promisekt.Promise
import me.jameshunt.flow.generated.GeneratedProfileController
import me.jameshunt.flow.generated.GeneratedProfileController.ProfileFlowState.*

open class ProfileBusinessFlowController: GeneratedProfileController() {
    override fun onGetProfile(state: GetProfile): Promise<FromGetProfile> {
        println("wow business logic")

        return ProfileManager
            .profile?.let { state.toDone(it) }
            ?: state.toProfileRequest()
    }

    override fun onProfileRequest(state: ProfileRequest): Promise<FromProfileRequest> {
        // network request
        Thread.sleep(5000)
        val networkResponse = Profile(
            email = "a@a.com",
            firstName = "wow",
            lastName = "last"
        )
        return state.toSaveProfile(networkResponse)
    }

    override fun onSaveProfile(state: SaveProfile): Promise<FromSaveProfile> {
        ProfileManager.profile = state.formData
        return state.toDone(state.formData)
    }
}