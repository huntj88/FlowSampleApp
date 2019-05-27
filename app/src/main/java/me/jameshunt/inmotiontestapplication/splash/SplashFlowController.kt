package me.jameshunt.inmotiontestapplication.splash

import com.inmotionsoftware.promisekt.Promise
import com.inmotionsoftware.promisekt.ensure
import com.inmotionsoftware.promisekt.map
import me.jameshunt.flow.generated.GeneratedSplashController
import me.jameshunt.flow.generated.GeneratedSplashController.SplashFlowState.*
import me.jameshunt.flow.promise.DispatchExecutor
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.TempFlowController
import me.jameshunt.inmotiontestapplication.colors.ColorFlowController
import me.jameshunt.inmotiontestapplication.group.pager.ViewPagerGroupController
import me.jameshunt.inmotiontestapplication.login.LoginFlowController
import me.jameshunt.inmotiontestapplication.settings.SettingsFlowController

class SplashFlowController : GeneratedSplashController() {

    private val splashFragment = proxy(SplashFragment::class.java)
    private val parkingLotFragment = proxy(ParkingLotFragment::class.java)

    override fun onSplash(state: Splash): Promise<FromSplash> {
        return this.flow(fragmentProxy = splashFragment, input = Unit).forResult<Unit, FromSplash>(
            onComplete = { Promise.value(Load) }
        )
    }

    override fun onLoad(state: Load): Promise<FromLoad> {
        return Promise.value(Unit)
            .ensure(on = DispatchExecutor.global) { Thread.sleep(1000) }
            .map { ParkingLot }
    }

    override fun onParkingLot(state: ParkingLot): Promise<FromParkingLot> {
        return this
            .flow(fragmentProxy = parkingLotFragment, input = Unit)
            .forResult<ParkingLotFragment.Output, FromParkingLot>(
                onBack = { Promise.value(Back) },
                onComplete = {
                    when (it) {
                        ParkingLotFragment.Output.Skip -> Promise.value(Home)
                        ParkingLotFragment.Output.Login -> Promise.value(Login)
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
            onBack = { Promise.value(Back) }
        )
    }

    override fun onLogin(state: Login): Promise<FromLogin> {
        return this.flow(controller = LoginFlowController::class.java, input = Unit).forResult<Unit, FromLogin>(
            onBack = { Promise.value(ParkingLot) },
            onComplete = { Promise.value(Home) }
        )
    }
}