package com.lc.stockhelper.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gyf.barlibrary.ImmersionBar
import com.lc.stockhelper.R
import com.lc.stockhelper.data.Stock
import com.lc.stockhelper.data.StockDetail
import com.lc.stockhelper.ui.adapter.SimpleFragmentPagerAdapter
import com.lc.stockhelper.ui.base.ToolbarActivity
import com.lc.stockhelper.ui.fragment.ChartKLineFragment
import kotlinx.android.synthetic.main.activity_stock_details.*

/**
 * Created by KID on 2019-10-11.
 */
class StockDetailsLandActivity : ToolbarActivity() {

    companion object {
        const val KEY_STOCK = "key_stock"
        const val KEY_DAY_DATA = "key_day_data"
        const val KEY_WEEK_DATA = "key_week_data"
        const val KEY_INDEX = "key_index"
    }

    override fun layoutId(): Int {
        return R.layout.activity_stock_detail_land
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dayData = intent.extras.getParcelableArrayList<StockDetail>(KEY_DAY_DATA)
        val weekData = intent.extras.getParcelableArrayList<StockDetail>(KEY_WEEK_DATA)

        val stock = intent.extras.getParcelable(KEY_STOCK) as Stock

        val index = intent.extras.getInt(KEY_INDEX)
        ImmersionBar.with(this)
            .statusBarColor(stock.getTypeColor())
            .fitsSystemWindows(true)
            .init()

        setTitleText(stock.name)
        setTitle2Text("(" + stock.code + ")")

        setToolbarBackground(stock.getTypeColor())
        enableNavigation()

        val fragments = arrayOf<Fragment>(
            ChartKLineFragment.newInstance(ChartKLineFragment.TYPE_DAY, dayData, true),
            ChartKLineFragment.newInstance(ChartKLineFragment.TYPE_WEEK, weekData, true)
        )
        val titles = arrayOf("日K", "周K")
        viewPager.setOffscreenPageLimit(fragments.size)
        viewPager.setAdapter(
            SimpleFragmentPagerAdapter(
                supportFragmentManager,
                fragments,
                titles
            )
        )
        tabLayout.setupWithViewPager(viewPager)
        viewPager.currentItem = index
    }

}