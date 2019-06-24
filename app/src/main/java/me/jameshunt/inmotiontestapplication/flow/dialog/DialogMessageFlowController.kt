package me.jameshunt.inmotiontestapplication.flow.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inmotionsoftware.promisekt.Promise
import com.inmotionsoftware.promisekt.thenMap
import kotlinx.android.synthetic.main.dialog_fragment_message.*
import me.jameshunt.flow.FlowDialogFragment
import me.jameshunt.flow.generated.GeneratedDialogTestController
import me.jameshunt.flow.generated.GeneratedDialogTestController.DialogTestFlowState.FromShowDialog
import me.jameshunt.flow.generated.GeneratedDialogTestController.DialogTestFlowState.ShowDialog
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.R

class DialogMessageFlowController : GeneratedDialogTestController() {

    override suspend fun onShowDialog(state: ShowDialog): FromShowDialog {
        return this.flow(
            fragmentProxy = proxy(DialogMessage::class.java),
            input = state.text
        ).let { state.toDone() }
    }
}

class DialogMessage : FlowDialogFragment<String, Unit>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        okButton.setOnClickListener { resolve(Unit) }
    }

    override fun flowWillRun(input: String) {
        messageTextView?.text = input
    }
}