package me.jameshunt.business

import me.jameshunt.flow.generated.GeneratedProfileController.ProfileFlowState.ProfileRequest
import me.jameshunt.flow.generated.GeneratedProfileController.ProfileFlowState.SaveProfile
import me.jameshunt.flowtest.flowTest
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class ProfileBusinessFlowControllerTest : ProfileBusinessFlowController() {

    init {
        flowTest {}
    }

    @Test
    fun onProfileRequestTest() {
        this.onProfileRequest(ProfileRequest)
            .map { assertTrue((it as SaveProfile).formData.firstName == "wow") }
            .catch { fail() }
    }
}