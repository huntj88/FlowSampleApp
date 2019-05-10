package me.jameshunt.inmotiontestapplication.colors

import me.jameshunt.flow.ViewId
import me.jameshunt.flow.generated.GeneratedColorController
import me.jameshunt.flow.generated.GeneratedColorController.ColorFlowState.*
import me.jameshunt.flow.promise.Promise
import me.jameshunt.flow.proxy

class ColorFlowController(viewId: ViewId): GeneratedColorController(viewId) {

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

        return Promise(ShowColors(colors))
    }

    override fun onShowColors(state: ShowColors): Promise<FromShowColors> {
        return this.flow(fragmentProxy = colorsListFragment, input = state.data).forResult<Color, FromShowColors>(
            onBack = { Promise(Back) },
            onComplete = { Promise(ColorSelected(it)) }
//            onComplete = { Promise(GatherData) }
        )
    }

    override fun onColorSelected(state: ColorSelected): Promise<FromColorSelected> {
        return this.flow(fragmentProxy = selectedColorFragment, input = state.data).forResult<Unit, FromColorSelected>(
            onBack = { Promise(GatherData) }
        )
    }
}