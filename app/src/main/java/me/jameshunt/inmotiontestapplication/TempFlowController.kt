package me.jameshunt.inmotiontestapplication

import com.inmotionsoftware.promisekt.Promise
import com.inmotionsoftware.promisekt.ensure
import com.inmotionsoftware.promisekt.thenMap
import me.jameshunt.flow.generated.GeneratedTempController
import me.jameshunt.flow.generated.GeneratedTempController.TempFlowState.*
import me.jameshunt.flow.promise.DispatchExecutor
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.flow.external.PhotoPickerFlowController
import me.jameshunt.inmotiontestapplication.flow.external.PhotoPickerFlowController.*
import me.jameshunt.inmotiontestapplication.splash.SplashFragment

open class TempFlowController : GeneratedTempController() {

    private val testFragmentProxy = proxy(TestFragment::class.java)
    private val splashFragmentProxy = proxy(SplashFragment::class.java)

    override fun onTemp(state: Temp): Promise<FromTemp> {
        return this.flow(fragmentProxy = testFragmentProxy, input = "wooooow").forResult(
            onBack = { state.toBack() },
            onComplete = { state.toSplash() }
        )
    }

    override fun onSplash(state: Splash): Promise<FromSplash> {
        return this.flow(fragmentProxy = splashFragmentProxy, input = Unit).forResult(
            onComplete = { state.toLoad() }
        )
    }

    override fun onLoad(state: Load): Promise<FromLoad> {
        return Promise.value(Unit)
            .ensure(on = DispatchExecutor.background) { Thread.sleep(3000) }
            .thenMap { state.toActivity() }
    }

    override fun onActivity(state: Activity): Promise<FromActivity> {
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