package me.jameshunt.inmotiontestapplication.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_parking_lot.*
import me.jameshunt.flow.FlowFragment
import me.jameshunt.inmotiontestapplication.R

class ParkingLotFragment : FlowFragment<Unit, ParkingLotFragment.Output>() {

    enum class Output {
        Skip,
        Login
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_parking_lot, container, false)
    }

    override fun flowWillRun(input: Unit) {
        this.loginButton.setOnClickListener {
            this.resolve(Output.Login)
        }

        this.skipText.setOnClickListener {
            this.resolve(Output.Skip)
        }
    }
}
