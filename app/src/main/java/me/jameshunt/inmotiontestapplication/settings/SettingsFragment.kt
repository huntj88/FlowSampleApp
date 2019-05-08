package me.jameshunt.inmotiontestapplication.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings.*
import me.jameshunt.flow.FlowFragment
import me.jameshunt.inmotiontestapplication.R

class SettingsFragment: FlowFragment<Unit, SettingsFragment.Output>() {

    enum class Output {
        UserSettings,
        Logout
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun flowWillRun(input: Unit) {
        this.userSettingsButton.setOnClickListener {
            this.resolve(Output.UserSettings)
        }

        this.logoutButton.setOnClickListener {
            this.resolve(Output.Logout)
        }
    }
}