package me.jameshunt.inmotiontestapplication

import com.inmotionsoftware.promisekt.Promise
import com.inmotionsoftware.promisekt.ensure
import com.inmotionsoftware.promisekt.map
import me.jameshunt.flow.generated.GeneratedTempController
import me.jameshunt.flow.generated.GeneratedTempController.TempFlowState.*
import me.jameshunt.flow.promise.DispatchExecutor
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.splash.SplashFragment

class TempFlowController : GeneratedTempController() {

    private val testFragmentProxy = proxy(TestFragment::class.java)
    private val splashFragmentProxy = proxy(SplashFragment::class.java)

    override fun onTemp(state: Temp): Promise<FromTemp> {
        return this.flow(fragmentProxy = testFragmentProxy, input = "wooooow").forResult<Unit, FromTemp>(
            onBack = { Promise.value(Back) },
            onComplete = { Promise.value(Splash) }
        )
    }

    override fun onSplash(state: Splash): Promise<FromSplash> {
        return this.flow(fragmentProxy = splashFragmentProxy, input = Unit).forResult<Unit, FromSplash>(
            onComplete = { Promise.value(Load) }
        )
    }

    override fun onLoad(state: Load): Promise<FromLoad> {
        return Promise.value(Unit)
            .ensure(on = DispatchExecutor.background) { Thread.sleep(3000) }
            .map { Temp }
    }
}