package me.jameshunt.business

import kotlinx.coroutines.delay
import me.jameshunt.flow.generated.GeneratedProfileController
import me.jameshunt.flow.generated.GeneratedProfileController.ProfileFlowState.*

open class ProfileBusinessFlowController: GeneratedProfileController() {
    override suspend fun onGetProfile(state: GetProfile): FromGetProfile {
        println("wow business logic")

        return ProfileManager
            .profile?.let { state.toDone(it) }
            ?: state.toProfileRequest()
    }

    override suspend fun onProfileRequest(state: ProfileRequest): FromProfileRequest {
        // network request
        delay(1000)
        val networkResponse = Profile(
            email = "a@a.com",
            firstName = "wow",
            lastName = "last"
        )
        return state.toSaveProfile(networkResponse)
    }

    override suspend fun onSaveProfile(state: SaveProfile): FromSaveProfile {
        ProfileManager.profile = state.formData
        return state.toDone(state.formData)
    }
}