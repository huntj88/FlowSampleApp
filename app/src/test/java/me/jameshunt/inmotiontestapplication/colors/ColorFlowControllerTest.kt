package me.jameshunt.inmotiontestapplication.colors

import com.inmotionsoftware.promisekt.*
import me.jameshunt.flow.generated.GeneratedColorController.ColorFlowState.*
import me.jameshunt.flowtest.enableTesting
import me.jameshunt.inmotiontestapplication.AsyncTests
import org.junit.Assert.*
import org.junit.Test

class ColorFlowControllerTest : ColorFlowController(), AsyncTests {

    init {
        this.enableTesting {}
    }

    @Test
    fun onGatherDataTest() {
        this.onGatherData(GatherData)
            .get { println(it) }
            .done { assertTrue((it as ShowColors).data.colors[2].blue == 255) }
            .throwCaughtErrors()
    }

    @Test
    fun onShowColorsFragmentMockTest() {
        this.enableTesting {

            // mock fragment and have it resolve with the value returned in this closure
            mockFragment(ColorsListFragment::class.java) { fragmentInput ->
                // mocked fragment output
                fragmentInput.colors.last()
            }
        }

        val data = Colors(
            listOf(
                Color(0, 0, 4),
                Color(0, 0, 5)
            )
        )

        this.onShowColors(state = ShowColors(data))
            .get { println(it) }
            .done {
                val color = (it as ColorSelected).data
                assertEquals(Color(0, 0, 5), color)
            }
            .throwCaughtErrors()
    }
}
