package com.lc.stockhelper.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.lc.stockhelper.R
import com.lc.stockhelper.ui.base.ToolbarActivity
import com.lc.stockhelper.ui.fragment.HistoryFragment
import com.lc.stockhelper.ui.fragment.PositionFragment
import com.lc.stockhelper.ui.fragment.StockListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabBar.setTitles(
            R.string.today_stock,
            R.string.self_selected,
            R.string.position,
            R.string.history_data
        )
            .setNormalIcons(
                R.drawable.ic_main_stock_normal,
                R.drawable.ic_main_self_normal,
                R.drawable.ic_main_position_normal,
                R.drawable.ic_main_history_normal
            )
            .setSelectedIcons(
                R.drawable.ic_main_stock_selected,
                R.drawable.ic_main_self_selected,
                R.drawable.ic_main_position_selected,
                R.drawable.ic_main_history_selected
            )
            .generate()
        tabBar.setContainer(viewPager)
        viewPager.offscreenPageLimit = 4
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> setTitleText(resources.getString(R.string.today_stock_title))
                    1 -> setTitleText(resources.getString(R.string.self_selected_title))
                    2 -> setTitleText(resources.getString(R.string.position))
                    3 -> setTitleText(resources.getString(R.string.history_data_title))
                }
            }
        })

        viewPager.adapter = object :
            FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            val fragments = ArrayList<Fragment>()

            init {
                fragments.add(StockListFragment.getTodayStockListFragment())
                fragments.add(StockListFragment.getSelfSelectedStockListFragment())
                fragments.add(PositionFragment())
                fragments.add(HistoryFragment())
            }

            override fun getItem(position: Int): Fragment {
                return fragments.get(position)
            }

            override fun getCount(): Int {
                return fragments.size
            }
        }
        setTitleText(resources.getString(R.string.today_stock_title))
    }


    override fun layoutId(): Int {
        return R.layout.activity_main
    }

}
