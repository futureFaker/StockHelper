package com.lc.stockhelper.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gyf.barlibrary.ImmersionBar
import com.lc.stockhelper.R
import com.lc.stockhelper.data.Stock
import com.lc.stockhelper.data.db.StockDao
import com.lc.stockhelper.data.server.StockServerApi
import com.lc.stockhelper.ui.adapter.SimpleFragmentPagerAdapter
import com.lc.stockhelper.ui.base.ToolbarActivity
import com.lc.stockhelper.ui.fragment.ChartKLineFragment
import com.lc.stockhelper.utils.DialogUtils
import kotlinx.android.synthetic.main.activity_stock_details.*
import kotlinx.coroutines.launch

/**
 * Created by KID on 2019-09-30.
 */
class StockDetailsActivity : ToolbarActivity() {

    companion object {
        const val KEY_STOCK = "key_stock"
    }

    override fun layoutId(): Int {
        return R.layout.activity_stock_details
    }

    lateinit var stock: Stock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stock = intent.getParcelableExtra<Stock>(KEY_STOCK)

        ImmersionBar.with(this)
            .statusBarColor(stock.getTypeColor())
            .fitsSystemWindows(true)
            .init()

        setTitleText(stock.name)
        setTitle2Text("(" + stock.code + ")")

        setToolbarBackground(stock.getTypeColor())
        enableNavigation()

        setRightIvClickListener {
            if (stock.selected) {
                stock.selected = false
                showStockSelected()
                launch {
                    StockDao.removeSelectedStock(stock)

                }
            } else {
                stock.selected = true
                showStockSelected()
                launch {
                    StockDao.addSelectedStock(stock)
                }
            }
        }

        setRightIv2ClickListener {
            if (!stock.bought) {
                DialogUtils.showBuyStockDialog(this) {
                    stock.bought = true
                    showStockBought()
                    launch {
                        StockDao.addBoughtStock(stock, it)
                    }
                }
            } else {
                DialogUtils.showSellStockDialog(this) {
                    stock.bought = false
                    showStockBought()
                    launch {
                        StockDao.removeBoughtStock(stock, it)
                    }
                }
            }
        }

        showStockSelected()
        showStockBought()
        getStockDetails()
    }

    private fun showStockSelected() {
        if (stock.selected) {
            setRightIvRes(R.drawable.ic_cancel_self_selected_1)
        } else {
            setRightIvRes(R.drawable.ic_add_self_selected_1)
        }
    }

    private fun showStockBought() {
        if (stock.bought) {
            setRightIv2Res(R.drawable.ic_sale_1)
        } else {
            setRightIv2Res(R.drawable.ic_buy_1)
        }
    }

    private fun getStockDetails() {
        launch {
            val dayData = StockServerApi.getStockDayDetails(stock)
            val weekData = StockServerApi.getStockWeekDetails(stock)

            val fragments = arrayOf<Fragment>(
                ChartKLineFragment.newInstance(
                    ChartKLineFragment.TYPE_DAY,
                    dayData,
                    false
                ).apply {
                    onClickListener = View.OnClickListener {
                        val intent =
                            Intent(this@StockDetailsActivity, StockDetailsLandActivity::class.java)
                        intent.putExtra(StockDetailsLandActivity.KEY_STOCK, stock)
                        intent.putParcelableArrayListExtra(StockDetailsLandActivity.KEY_DAY_DATA, dayData)
                        intent.putParcelableArrayListExtra(StockDetailsLandActivity.KEY_WEEK_DATA, weekData)
                        intent.putExtra(StockDetailsLandActivity.KEY_INDEX, 0)
                        startActivity(intent)
                    }
                },
                ChartKLineFragment.newInstance(
                    ChartKLineFragment.TYPE_WEEK,
                    weekData,
                    false
                ).apply {
                    onClickListener = View.OnClickListener {
                        val intent =
                            Intent(this@StockDetailsActivity, StockDetailsLandActivity::class.java)
                        intent.putExtra(StockDetailsLandActivity.KEY_STOCK, stock)
                        intent.putParcelableArrayListExtra(StockDetailsLandActivity.KEY_DAY_DATA, dayData)
                        intent.putParcelableArrayListExtra(StockDetailsLandActivity.KEY_WEEK_DATA, weekData)
                        intent.putExtra(StockDetailsLandActivity.KEY_INDEX, 1)
                        startActivity(intent)
                    }
                }
                //ChartKLineFragment.newInstance(30, false)
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

        }
    }

}