package me.jameshunt.inmotiontestapplication.flow.dialog

import com.inmotionsoftware.promisekt.Promise
import me.jameshunt.flow.FlowResult
import me.jameshunt.flow.FragmentFlowController
import me.jameshunt.flow.FragmentProxy
import me.jameshunt.flow.proxy

interface DialogFlow {
    fun FragmentFlowController<*, *>.showDialog(text: String): Promise<FlowResult<Unit>>
}

class DialogFlowImpl : DialogFlow {

    private var simpleDialog: FragmentProxy<String, Unit, DialogMessage>? = null

    private fun FragmentFlowController<*, *>.simpleDialog(): FragmentProxy<String, Unit, DialogMessage> {
        return simpleDialog
            ?: this
                .proxy(DialogMessage::class.java)
                .apply { simpleDialog = this }
    }

    override fun FragmentFlowController<*, *>.showDialog(text: String): Promise<FlowResult<Unit>> {
        return this.flow(fragmentProxy = simpleDialog(), input = text)
    }
}

