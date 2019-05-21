package me.jameshunt.inmotiontestapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.jameshunt.flow.FlowFragment

class TestFragment: FlowFragment<String, Unit>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(context).apply {
            if(text.isBlank()) {
                this.text = "no data"
            }
            setOnClickListener {
                this@TestFragment.resolve(Unit)
            }
        }
    }

    override fun flowWillRun(input: String) {
        (this.view as TextView).text = input
    }
}