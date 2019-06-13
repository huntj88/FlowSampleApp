package me.jameshunt.inmotiontestapplication.colors

import com.inmotionsoftware.promisekt.*
import me.jameshunt.flow.generated.GeneratedColorController.ColorFlowState.*
import me.jameshunt.flowtest.flowTest
import me.jameshunt.inmotiontestapplication.AsyncTests
import org.junit.Assert.*
import org.junit.Test

class ColorFlowControllerTest : ColorFlowController(), AsyncTests {

    init {
        flowTest {}
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

        val data = Colors(
            listOf(
                Color(0, 0, 4),
                Color(0, 0, 5)
            )
        )

        flowTest {
            mockFragment(ColorsListFragment::class.java) { fragmentInput ->
                // mocked fragment output
                fragmentInput.colors.last()
            }
        }

        this.onShowColors(state = ShowColors(data))
            .get { println(it) }
            .done {
                val color = (it as ColorSelected).data
                assertEquals(Color(0, 0, 5), color)
            }
            .throwCaughtErrors()
    }

    @Test
    fun throwExceptionFragmentMockTest() {

        flowTest {
            mockFragment(ColorsListFragment::class.java) { fragmentInput ->
                // mocked fragment output
                throw IllegalStateException("test exception")
            }
        }

        this.onShowColors(state = ShowColors(Colors(listOf())))
            .done { fail() }
            .catch {
                assertTrue("caught exception", true)
            }
    }
}
