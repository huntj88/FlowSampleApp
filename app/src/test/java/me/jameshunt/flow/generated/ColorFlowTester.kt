package me.jameshunt.flow.generated

import com.inmotionsoftware.promisekt.Promise
import com.inmotionsoftware.promisekt.map
import me.jameshunt.flow.FlowController
import me.jameshunt.inmotiontestapplication.colors.Color
import me.jameshunt.inmotiontestapplication.colors.ColorFlowController
import me.jameshunt.inmotiontestapplication.colors.Colors
import org.junit.Test


class ColorFlowTester {

    val flow = ColorFlowController()

    val colorFlowJavaTester = ColorFlowJavaTester()

    @Test
    fun blah() {

        val clazz = flow::class.java

        val onGatherData = clazz
            .getDeclaredMethod("onGatherData", colorFlowJavaTester.GatherDataClass())
            .apply { isAccessible = true }

        val input = colorFlowJavaTester
            .GatherDataClass()
            .getDeclaredConstructor()
            .apply { isAccessible = true }
            .newInstance()

        val anyyy = onGatherData.invoke(flow, input) as Promise<FlowController.State>

        anyyy.map {
            val output = colorFlowJavaTester.showColorsClass().cast(it)
            println(output)

            val sup = colorFlowJavaTester.showColorsClass().getDeclaredField("data").let {
                it.isAccessible = true
                it.get(output) as Colors
            }
            println(sup)
        }
    }

//    private fun invokeHiddenMethod(name: String) {
//        val method = flow.javaClass.getDeclaredMethod(name)
//        method.isAccessible = true
//        method.invoke(testSubject)
//    }

}