package com.lc.stockhelper.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lc.stockhelper.R
import com.lc.stockhelper.data.db.StockDao
import com.lc.stockhelper.ui.StockListActivity
import com.lc.stockhelper.ui.adapter.HistoryStockListAdapter
import com.lc.stockhelper.ui.base.CoroutineFragment
import kotlinx.android.synthetic.main.fragment_history_stocks.*
import kotlinx.android.synthetic.main.item_history_stock.view.*
import kotlinx.coroutines.launch

/**
 * Created by KID on 2019-10-15.
 */
class HistoryStocksFragment : CoroutineFragment() {

    private val datas = ArrayList<String>()
    private val adapter = HistoryStockListAdapter(datas) { view: View, data: String ->
        val intent = Intent(activity, StockListActivity::class.java)
        intent.putExtra(StockListActivity.KEY_DATA, data)
        val pair =
            Pair(view.tv as View, getString(R.string.transition_name_stock_name))

        val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity as Activity, pair
        )
        ActivityCompat.startActivity(context!!, intent, activityOptionsCompat.toBundle())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_stocks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        launch {
            datas.clear();
            datas.addAll(StockDao.getStockTableNames())
            adapter.notifyDataSetChanged()
        }
    }

}