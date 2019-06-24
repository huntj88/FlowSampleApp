package me.jameshunt.business

import kotlinx.coroutines.runBlocking
import me.jameshunt.flow.generated.GeneratedProfileController.ProfileFlowState.*
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
        runBlocking {
            try {
                onProfileRequest(ProfileRequest).let {
                    assertTrue((it as SaveProfile).formData.firstName == "wow")
                }
            } catch (t: Throwable) {
                fail()
            }
        }
    }
}