package me.jameshunt.inmotiontestapplication.splash

import me.jameshunt.flow.ViewId
import me.jameshunt.flow.generated.GeneratedSplashController
import me.jameshunt.flow.generated.GeneratedSplashController.SplashFlowState.*
import me.jameshunt.flow.promise.Promise
import me.jameshunt.flow.promise.PromiseDispatch
import me.jameshunt.flow.promise.doAlso
import me.jameshunt.flow.promise.then
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.TempFlowController
import me.jameshunt.inmotiontestapplication.colors.ColorFlowController
import me.jameshunt.inmotiontestapplication.group.pager.ViewPagerGroupController
import me.jameshunt.inmotiontestapplication.login.LoginFlowController
import me.jameshunt.inmotiontestapplication.settings.SettingsFlowController

class SplashFlowController(viewId: ViewId) : GeneratedSplashController(viewId) {

    private val splashFragment = proxy(SplashFragment::class.java)
    private val parkingLotFragment = proxy(ParkingLotFragment::class.java)

    override fun onSplash(state: Splash): Promise<FromSplash> {
        return this.flow(fragmentProxy = splashFragment, input = Unit).forResult<Unit, FromSplash>(
            onComplete = { Promise(Load) }
        )
    }

    override fun onLoad(state: Load): Promise<FromLoad> {
        return Promise(Unit)
            .doAlso(on = PromiseDispatch.BACKGROUND) { Thread.sleep(1000) }
            .then { ParkingLot }
    }

    override fun onParkingLot(state: ParkingLot): Promise<FromParkingLot> {
        return this
            .flow(fragmentProxy = parkingLotFragment, input = Unit)
            .forResult<ParkingLotFragment.Output, FromParkingLot>(
                onBack = { Promise(Back) },
                onComplete = {
                    when (it) {
                        ParkingLotFragment.Output.Skip -> Promise(Home)
                        ParkingLotFragment.Output.Login -> Promise(Login)
                    }
                }
            )
    }

    override fun onHome(state: Home): Promise<FromHome> {
        val input = ViewPagerGroupController.input(
            pageZero = TempFlowController::class.java,
            pageOne = ColorFlowController::class.java,
            pageTwo = SettingsFlowController::class.java
        )
        return this.flowGroup(ViewPagerGroupController::class.java, input).forResult<Unit, FromHome>(
            onBack = { Promise(Back) }
        )
    }

    override fun onLogin(state: Login): Promise<FromLogin> {
        return this.flow(LoginFlowController::class.java, Unit).forResult<Unit, FromLogin>(
            onBack = { Promise(ParkingLot) },
            onComplete = { Promise(Home) }
        )
    }
}