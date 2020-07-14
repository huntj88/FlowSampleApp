package me.jameshunt.inmotiontestapplication.colors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.jameshunt.flow.FlowDialogFragment
import me.jameshunt.flow.FlowUIInput

class SelectedColorFragment: FlowDialogFragment<Color, Unit>() {

    private lateinit var selectedColor: Color

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View(context).apply {
            savedInstanceState
                ?.getParcelable<Color>("color")
                ?.also { selectedColor = it }
                ?.also { this.setBackgroundColor(it.toAndroidColor()) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnClickListener {
            resolve(Unit)
        }
    }

    override fun onResume() {
        super.onResume()
        (getAndConsumeInputData() as? FlowUIInput.NewData)?.let { color ->
            selectedColor = color.data
            this.view?.setBackgroundColor(selectedColor.toAndroidColor())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("color", selectedColor)
    }

    private fun Color.toAndroidColor(): Int {
        return android.graphics.Color.rgb(this.red, this.green, this.blue)
    }
}