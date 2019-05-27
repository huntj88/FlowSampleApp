package me.jameshunt.inmotiontestapplication.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.jameshunt.flow.FlowFragment
import me.jameshunt.inmotiontestapplication.R

class SplashFragment : FlowFragment<Unit, Unit>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun flowWillRun(input: Unit) {
        this.resolve(Unit)
    }
}