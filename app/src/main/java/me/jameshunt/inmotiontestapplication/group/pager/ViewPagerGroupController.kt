package me.jameshunt.inmotiontestapplication.group.pager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.jameshunt.flow.FragmentFlowController
import me.jameshunt.flow.FragmentGroupFlowController
import me.jameshunt.flow.promise.Promise
import me.jameshunt.flow.promise.firstToResolve
import me.jameshunt.inmotiontestapplication.R
import me.jameshunt.inmotiontestapplication.group.pager.ViewPagerGroupController.*

class ViewPagerGroupController : FragmentGroupFlowController<InternalInput, Unit>(R.layout.group_view_pager) {

    companion object {
        fun <A, B, C> input(
            pageZero: Class<A>,
            pageOne: Class<B>,
            pageTwo: Class<C>
        ): InternalInput
                where A : FragmentFlowController<Unit, Unit>,
                      B : FragmentFlowController<Unit, Unit>,
                      C : FragmentFlowController<Unit, Unit> {
            return InternalInput(
                pageZero = pageZero as Class<FragmentFlowController<Unit, Unit>>,
                pageOne = pageOne as Class<FragmentFlowController<Unit, Unit>>,
                pageTwo = pageTwo as Class<FragmentFlowController<Unit, Unit>>
            )
        }
    }

    data class InternalInput(
        val pageZero: Class<FragmentFlowController<Unit, Unit>>,
        val pageOne: Class<FragmentFlowController<Unit, Unit>>,
        val pageTwo: Class<FragmentFlowController<Unit, Unit>>
    ) : GroupInput()

    private var backIndex = 0

    override fun childIndexToDelegateBack(): Int = backIndex

    override fun setupGroup(layout: ViewGroup) {
        val viewPager = layout.findViewById<ViewPager>(R.id.groupViewPager)

        val inflater = LayoutInflater.from(viewPager.context)

        val pages = (0..2).map { index ->
            when (index) {
                0 -> inflater.inflate(R.layout.group_view_pager_zero, viewPager, false)
                1 -> inflater.inflate(R.layout.group_view_pager_one, viewPager, false)
                2 -> inflater.inflate(R.layout.group_view_pager_two, viewPager, false)
                else -> throw NotImplementedError()
            }.also { viewPager.addView(it) }
        }

        viewPager.offscreenPageLimit = pages.size - 1

        viewPager.adapter = object : PagerAdapter() {
            override fun instantiateItem(collection: ViewGroup, position: Int): Any = pages[position]

            override fun getCount(): Int = pages.size

            override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                // no op
            }
        }

        val bottomNav = layout.findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.one -> viewPager.currentItem = 0
                R.id.two -> viewPager.currentItem = 1
                R.id.three -> viewPager.currentItem = 2
            }
            true
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                backIndex = position
                bottomNav.menu.getItem(position).isChecked = true
            }
        })

        viewPager.currentItem = childIndexToDelegateBack()
    }

    override fun startFlowInGroup(groupInput: InternalInput): Promise<State> {
        val pageZero = this.flow(controller = groupInput.pageZero, viewId = R.id.groupPagerZero, input = Unit)
        val pageOne = this.flow(controller = groupInput.pageOne, viewId = R.id.groupPagerOne, input = Unit)
        val pageTwo = this.flow(controller = groupInput.pageTwo, viewId = R.id.groupPagerTwo, input = Unit)

        return listOf(pageZero, pageOne, pageTwo).firstToResolve().forResult<Unit, State>(
            onBack = { Promise(Back) },
            onComplete = { Promise(Done(it)) }
        )
    }
}