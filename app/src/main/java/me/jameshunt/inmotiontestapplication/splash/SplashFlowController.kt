package me.jameshunt.inmotiontestapplication.splash

import kotlinx.coroutines.delay
import me.jameshunt.flow.generated.GeneratedSplashController
import me.jameshunt.flow.generated.GeneratedSplashController.SplashFlowState.*
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.TempFlowController
import me.jameshunt.inmotiontestapplication.colors.ColorFlowController
import me.jameshunt.inmotiontestapplication.flow.group.pager.ViewPagerGroupController
import me.jameshunt.inmotiontestapplication.login.LoginFlowController
import me.jameshunt.inmotiontestapplication.settings.SettingsFlowController

class SplashFlowController : GeneratedSplashController() {

    private val splashFragment = proxy(SplashFragment::class.java)
    private val parkingLotFragment = proxy(ParkingLotFragment::class.java)

    override suspend fun onSplash(state: Splash): FromSplash {
        return this.flow(fragmentProxy = splashFragment, input = Unit).forResult(
            onComplete = { state.toLoad() }
        )
    }

    override suspend fun onLoad(state: Load): FromLoad {
        delay(1000)
        return state.toParkingLot()
    }

    override suspend fun onParkingLot(state: ParkingLot): FromParkingLot {
        return this
            .flow(fragmentProxy = parkingLotFragment, input = Unit)
            .forResult(
                onBack = { state.toBack() },
                onComplete = {
                    when (it) {
                        ParkingLotFragment.Output.Skip -> state.toHome()
                        ParkingLotFragment.Output.Login -> state.toLogin()
                    }
                }
            )
    }

    override suspend fun onHome(state: Home): FromHome {
        val input = ViewPagerGroupController.input(
            pageZero = TempFlowController::class.java,
            pageOne = ColorFlowController::class.java,
            pageTwo = SettingsFlowController::class.java
        )
        return this.flowGroup(ViewPagerGroupController::class.java, input).forResult(
            onBack = { state.toBack() }
        )
    }

    override suspend fun onLogin(state: Login): FromLogin {
        return this.flow(controller = LoginFlowController::class.java, input = Unit).forResult(
            onBack = { state.toParkingLot() },
            onComplete = { state.toHome() }
        )
    }
}