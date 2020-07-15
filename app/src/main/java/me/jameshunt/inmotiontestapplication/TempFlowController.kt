package me.jameshunt.inmotiontestapplication

import kotlinx.coroutines.delay
import me.jameshunt.flow.generated.GeneratedTempController
import me.jameshunt.flow.generated.GeneratedTempController.TempFlowState.*
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.flow.external.PhotoPickerFlowController
import me.jameshunt.inmotiontestapplication.flow.external.PhotoPickerFlowController.*
import me.jameshunt.inmotiontestapplication.splash.SplashFragment

open class TempFlowController : GeneratedTempController() {

    private val testFragmentProxy = proxy(TestFragment::class.java)
    private val splashFragmentProxy = proxy(SplashFragment::class.java)

    override suspend fun onTemp(state: Temp): FromTemp {
        return this.flow(fragmentProxy = testFragmentProxy, input = "wooooow").forResult(
            onBack = { state.toBack() },
            onComplete = { state.toSplash() }
        )
    }

    override suspend fun onSplash(state: Splash): FromSplash {
        return this.flow(fragmentProxy = splashFragmentProxy, input = Unit).forResult(
            onComplete = { state.toLoad() }
        )
    }

    override suspend fun onLoad(state: Load): FromLoad {
        delay(3000)
        return state.toActivity()
    }

    override suspend fun onActivity(state: Activity): FromActivity {
        return this.flow(
            controller = PhotoPickerFlowController::class.java,
            input = PhotoType.Uri
        ).forResult(
            onComplete = {
                println("woohoooooo imageUri: $it")
                state.toTemp()
            },
            onBack = {
                println("back from activity")
                state.toTemp()
            })
    }
}