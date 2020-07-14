package me.jameshunt.inmotiontestapplication.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_not_logged_in.*
import me.jameshunt.flow.FlowFragment
import me.jameshunt.inmotiontestapplication.R

class NotLoggedInFragment: FlowFragment<Unit, Unit>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_not_logged_in, container, false)
    }

    override fun onResume() {
        super.onResume()
        this.loginButton.setOnClickListener {
            this.resolve(Unit)
        }
    }
}