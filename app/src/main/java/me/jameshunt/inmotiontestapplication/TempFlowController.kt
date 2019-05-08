package me.jameshunt.inmotiontestapplication

import me.jameshunt.flow.*
import me.jameshunt.flow.generated.GeneratedTempController
import me.jameshunt.flow.generated.GeneratedTempController.TempFlowState.*
import me.jameshunt.flow.promise.Promise
import me.jameshunt.flow.promise.PromiseDispatch
import me.jameshunt.flow.promise.doAlso
import me.jameshunt.flow.promise.then
import me.jameshunt.inmotiontestapplication.login.LoginFlowController
import me.jameshunt.inmotiontestapplication.splash.SplashFragment

class TempFlowController(viewId: ViewId) : GeneratedTempController(viewId) {

    private val testFragmentProxy = proxy(TestFragment::class.java)
    private val splashFragmentProxy = proxy(SplashFragment::class.java)

    override fun onTemp(state: Temp): Promise<FromTemp> {
        return this.flow(fragmentProxy = testFragmentProxy, input = "wooooow").forResult<Unit, FromTemp>(
            onBack = { Promise(Back) },
            onComplete = { Promise(Splash) }
        )
    }

    override fun onSplash(state: Splash): Promise<FromSplash> {
        return this.flow(fragmentProxy = splashFragmentProxy, input = Unit).forResult<Unit, FromSplash>(
            onComplete = { Promise(Load) }
        )
    }

    override fun onLoad(state: Load): Promise<FromLoad> {
        return Promise(Unit)
            .doAlso(on = PromiseDispatch.BACKGROUND) { Thread.sleep(1000) }
            .then { Temp }
    }
}