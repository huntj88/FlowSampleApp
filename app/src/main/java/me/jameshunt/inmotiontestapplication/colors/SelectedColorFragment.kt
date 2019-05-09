package me.jameshunt.inmotiontestapplication.colors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.jameshunt.flow.FlowFragment

class SelectedColorFragment: FlowFragment<Color, Unit>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View(context)
    }

    override fun flowWillRun(input: Color) {
        val color = input.let { android.graphics.Color.rgb(it.red, it.green, it.blue) }
        this.view?.setBackgroundColor(color)
    }
}