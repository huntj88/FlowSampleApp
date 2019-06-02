package me.jameshunt.inmotiontestapplication.colors

import com.inmotionsoftware.promisekt.Promise
import me.jameshunt.flow.generated.GeneratedColorController
import me.jameshunt.flow.generated.GeneratedColorController.ColorFlowState.*
import me.jameshunt.flow.proxy

open class ColorFlowController: GeneratedColorController() {

    private val colorsListFragment = proxy(ColorsListFragment::class.java)
    private val selectedColorFragment = proxy(SelectedColorFragment::class.java)

    override fun onGatherData(state: GatherData): Promise<FromGatherData> {
        val colors = Colors(listOf(
            Color(255,0,0),
            Color(0,255,0),
            Color(0,0,255),
            Color(255,255,0),
            Color(255,0,255),
            Color(255,255,255),
            Color(0,255,255),
            Color((0..255).random(),(0..255).random(),(0..255).random()),
            Color(125,125,125),
            Color(255,0,0),
            Color(0,255,0),
            Color(0,0,255),
            Color(255,255,0),
            Color(255,0,255),
            Color(0,255,255),
            Color(0,0,0),
            Color(255,255,255),
            Color(125,125,125)
        ))

        return state.toShowColors(colors)
    }

    override fun onShowColors(state: ShowColors): Promise<FromShowColors> {
        return this.flow(fragmentProxy = colorsListFragment, input = state.data).forResult(
            onBack = { state.toBack() },
            onComplete = { state.toColorSelected(it) }
//            onComplete = { Promise(GatherData) }
        )
    }

    override fun onColorSelected(state: ColorSelected): Promise<FromColorSelected> {
        return this.flow(fragmentProxy = selectedColorFragment, input = state.data).forResult(
            onComplete = { state.toGatherData() },
            onBack = { state.toGatherData() }
        )
    }
}