package com.lc.stockhelper.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lc.stockhelper.R
import com.lc.stockhelper.data.BoughtStock
import com.lc.stockhelper.data.db.StockDao
import com.lc.stockhelper.data.event.StockActionEvent
import com.lc.stockhelper.ui.adapter.BoughtStockListAdapter
import com.lc.stockhelper.ui.base.CoroutineFragment
import com.lc.stockhelper.utils.DialogUtils
import com.lc.stockhelper.utils.EventBusUtils
import kotlinx.android.synthetic.main.fragment_history_actions.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by KID on 2019-10-15.
 */
class PositionFragment : CoroutineFragment() {

    private val datas = ArrayList<BoughtStock>()
    private val adapter = BoughtStockListAdapter(datas)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_position, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val onItemActionListener = object :
            BoughtStockListAdapter.OnItemActionListener {
            override fun onItemClick(view: View, boughtStock: BoughtStock) {

            }

            override fun onItemBoughtClick(position: Int, boughtStock: BoughtStock) {

                DialogUtils.showSellStockDialog(activity as Activity) {
                    launch {
                        StockDao.removeBoughtStock(boughtStock.stock, it)
                    }
                    datas.remove(boughtStock)
                    adapter.notifyItemRemoved(position)
                }
            }
        }
        adapter.onItemActionListener = onItemActionListener
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        getDatas()
        EventBusUtils.register(this)
    }

    private fun getDatas() {
        launch {
            datas.clear();
            datas.addAll(StockDao.getBoughtStocks())
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        EventBusUtils.unRegister(this)
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onHandleStockActionEvent(stockActionEvent: StockActionEvent) {
        getDatas()
    }
}