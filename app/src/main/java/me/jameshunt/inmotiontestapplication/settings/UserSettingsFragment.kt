package me.jameshunt.inmotiontestapplication.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_user_settings.*
import me.jameshunt.flow.FlowFragment
import me.jameshunt.inmotiontestapplication.R
import me.jameshunt.inmotiontestapplication.settings.UserSettingsFragment.*

class UserSettingsFragment: FlowFragment<Data, Data>() {

    data class Data(
        val firstName: String,
        val lastName: String
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_settings, container, false)
    }

    override fun flowWillRun(input: Data) {
        this.firstNameField.setText(input.firstName)
        this.lastNameField.setText(input.lastName)
        this.doneButton.setOnClickListener {
            val output = Data(this.firstNameField.text.toString(), this.lastNameField.text.toString())
            this.resolve(output)
        }
    }
}