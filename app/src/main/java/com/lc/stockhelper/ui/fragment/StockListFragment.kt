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
import androidx.recyclerview.widget.LinearLayoutManager
import com.lc.stockhelper.R
import com.lc.stockhelper.data.Stock
import com.lc.stockhelper.data.db.StockDao
import com.lc.stockhelper.data.event.SelectedStockChangeEvent
import com.lc.stockhelper.data.server.StockServerApi
import com.lc.stockhelper.ui.StockDetailsActivity
import com.lc.stockhelper.ui.adapter.StockListAdapter
import com.lc.stockhelper.ui.base.CoroutineFragment
import com.lc.stockhelper.utils.DialogUtils
import com.lc.stockhelper.utils.EventBusUtils
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.fragment_stock_list.*
import kotlinx.android.synthetic.main.item_stock.view.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by KID on 2019-09-30.
 */
class StockListFragment : CoroutineFragment() {

    companion object {

        const val KEY_TYPE = "key_type"
        const val KEY_TABLE_NAME = "key_table_name"

        const val TYPE_TODAY = 1
        const val TYPE_SELECTED = 2
        const val TYPE_HISTORY = 3

        fun getTodayStockListFragment(): StockListFragment {
            val fragment = StockListFragment()
            val bundle = Bundle()
            bundle.putInt(
                KEY_TYPE,
                TYPE_TODAY
            )
            fragment.arguments = bundle
            return fragment
        }

        fun getSelfSelectedStockListFragment(): StockListFragment {
            val fragment = StockListFragment()
            val bundle = Bundle()
            bundle.putInt(
                KEY_TYPE,
                TYPE_SELECTED
            )
            fragment.arguments = bundle
            return fragment
        }

        fun getHistoryStockListFragment(table:String): StockListFragment {
            val fragment = StockListFragment()
            val bundle = Bundle()
            bundle.putInt(
                KEY_TYPE,
                TYPE_HISTORY
            )
            bundle.putString(KEY_TABLE_NAME,table)
            fragment.arguments = bundle
            return fragment
        }
    }

    var type = TYPE_TODAY
    val datas = ArrayList<Stock>()
    val adapter = StockListAdapter(datas)
    lateinit var tableName: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(com.lc.stockhelper.R.layout.fragment_stock_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = arguments!!.getInt(KEY_TYPE)
        if(type == TYPE_HISTORY){
            tableName = arguments!!.getString(KEY_TABLE_NAME)
            adapter.showAction = false
        }

        val onItemActionListener = object :
            StockListAdapter.OnItemActionListener {
            override fun onItemClick(view: View, stock: Stock) {

                if (type == TYPE_HISTORY) {
                    return
                }
                val intent = Intent(activity, StockDetailsActivity::class.java)
                intent.putExtra(StockDetailsActivity.KEY_STOCK, stock)
                val stockCodePair =
                    Pair(view.stockCode as View, getString(R.string.transition_name_stock_code))
                val stockNamePair =
                    Pair(view.stockName as View, getString(R.string.transition_name_stock_name))
                val stockSelectedPair = Pair(
                    view.stockSelected as View,
                    getString(R.string.transition_name_stock_selected)
                )
                val stockBoughtPair = Pair(
                    view.stockBought as View,
                    getString(R.string.transition_name_stock_bought)
                )
                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity as Activity,
                    stockCodePair,
                    stockNamePair,
                    stockSelectedPair,
                    stockBoughtPair
                )
                ActivityCompat.startActivity(context!!, intent, activityOptionsCompat.toBundle())
                //startActivity<StockDetailsActivity>(StockDetailsActivity.KEY_STOCK to stock)
            }

            override fun onItemSelectedClick(position: Int, stock: Stock) {
                if (type == TYPE_HISTORY) {
                    return
                }
                if (!stock.selected) {
                    stock.selected = true
                    launch {
                        StockDao.addSelectedStock(stock)
                    }
                    adapter.notifyItemChanged(position)
                } else {
                    stock.selected = false
                    launch {
                        StockDao.removeSelectedStock(stock)
                    }
                    if (type == TYPE_SELECTED) {
                        datas.remove(stock)
                        adapter.notifyItemRemoved(position)
                    } else {
                        adapter.notifyItemChanged(position)
                    }
                }
            }

            override fun onItemBoughtClick(position: Int, stock: Stock) {
                if (type == TYPE_HISTORY) {
                    return
                }
                if (!stock.bought) {
                    DialogUtils.showBuyStockDialog(activity as Activity) {
                        stock.bought = true
                        launch {
                            StockDao.addBoughtStock(stock, it)
                        }
                        adapter.notifyItemChanged(position)
                    }
                } else {
                    DialogUtils.showSellStockDialog(activity as Activity) {
                        stock.bought = false
                        launch {
                            StockDao.removeBoughtStock(stock, it)
                        }
                        adapter.notifyItemChanged(position)
                    }
                }
            }
        }

        adapter.onItemActionListener = onItemActionListener
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = SlideInRightAnimator()
        recyclerView.itemAnimator!!.apply {
            addDuration = 250
            removeDuration = 500
        }
        launch {
            if (type == TYPE_TODAY) {
                getTodayStockDatas()
            } else if (type == TYPE_SELECTED) {
                getSelectedDatas()
            } else {
                getHistoryStockDatas()
            }
            adapter.notifyItemRangeInserted(0, datas.size)
        }
        EventBusUtils.register(this)
    }

    override fun onDestroyView() {
        EventBusUtils.unRegister(this)
        super.onDestroyView()
    }

    private suspend fun getTodayStockDatas() {
        var temp = StockServerApi.getTodayStockList()
        if (temp.isEmpty()) {
            return
        }
        StockDao.removeTodayStocks()
        StockDao.addTodayStocks(temp)
        val selectedStocks = StockDao.getSelectedStocks()
        val boughtStocks = StockDao.getBoughtStocks().map {
            it.stock
        }
        temp.map {
            if (it in selectedStocks) {
                it.selected = true
            } else {
                it.selected = false
            }

            if (it in boughtStocks) {
                it.bought = true
            } else {
                it.bought = false
            }
        }
        datas.clear()
        datas.addAll(temp)
    }

    private suspend fun getSelectedDatas() {
        val temp = StockDao.getSelectedStocks()

        val boughtStocks = StockDao.getBoughtStocks().map {
            it.stock
        }

        temp.map {
            if (it in boughtStocks) {
                it.bought = true
            } else {
                it.bought = false
            }
        }
        datas.clear()
        datas.addAll(temp)
        println("getSelectedDatas " + datas)
    }

    private suspend fun getHistoryStockDatas(){
        val temp = StockDao.getHistoryStocks(tableName)
        datas.clear()
        datas.addAll(temp)
        println("getHistoryStockDatas " + datas)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onHandleSelctedStockChangeEvent(selectedStockChangeEvent: SelectedStockChangeEvent) {
        if (type == TYPE_SELECTED) {
            launch {
                getSelectedDatas()
                adapter.notifyDataSetChanged()
            }
        } else if(type == TYPE_TODAY){
            launch {
                getTodayStockDatas()
                adapter.notifyDataSetChanged()
            }
        }
    }

}