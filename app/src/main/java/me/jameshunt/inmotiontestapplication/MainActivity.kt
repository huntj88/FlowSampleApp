package me.jameshunt.inmotiontestapplication

import me.jameshunt.flow.FlowActivity
import me.jameshunt.inmotiontestapplication.splash.SplashFlowController

class MainActivity : FlowActivity<SplashFlowController>() {
    override fun getInitialFlow(): Class<SplashFlowController> = SplashFlowController::class.java
}
