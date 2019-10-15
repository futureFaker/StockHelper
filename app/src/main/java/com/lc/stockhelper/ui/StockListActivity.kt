package com.lc.stockhelper.ui

import android.os.Bundle
import com.lc.stockhelper.R
import com.lc.stockhelper.data.Stock
import com.lc.stockhelper.ui.base.ToolbarActivity
import com.lc.stockhelper.ui.fragment.StockListFragment
import com.lc.stockhelper.utils.getYYYYMMDDFromTimeStamp

/**
 * Created by KID on 2019-10-15.
 */
class StockListActivity : ToolbarActivity() {

    companion object {

        const val KEY_DATA = "key_data"

    }

    override fun layoutId(): Int {
        return R.layout.activity_stock_list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.extras.getString(KEY_DATA)

        val time = data.replace(Stock.StaticO.TABLE_NAME, "")
        setTitleText(getYYYYMMDDFromTimeStamp(time.toLong()))

        supportFragmentManager.beginTransaction()
            .add(R.id.container, StockListFragment.getHistoryStockListFragment(data)).commit()
    }

}