package me.jameshunt.inmotiontestapplication.profile

import com.inmotionsoftware.promisekt.Promise
import com.inmotionsoftware.promisekt.ensure
import me.jameshunt.flow.FlowController
import me.jameshunt.flow.promise.DispatchExecutor

class ProfileBusinessFlowController: FlowController<Unit, Unit>() {
    override fun onStart(state: InitialState<Unit>) {
        println("wow business logic")
        Promise.value(Unit)
            .ensure(on = DispatchExecutor.background) { Thread.sleep(3000) }
            .ensure { this.onDone(Unit) }
    }
}