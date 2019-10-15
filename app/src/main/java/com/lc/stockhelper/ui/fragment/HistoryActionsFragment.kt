package com.lc.stockhelper.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lc.stockhelper.R
import com.lc.stockhelper.data.StockAction
import com.lc.stockhelper.data.db.StockDao
import com.lc.stockhelper.data.event.StockActionEvent
import com.lc.stockhelper.ui.adapter.HistoryActionListAdapter
import com.lc.stockhelper.ui.base.CoroutineFragment
import com.lc.stockhelper.utils.EventBusUtils
import kotlinx.android.synthetic.main.fragment_history_actions.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by KID on 2019-10-15.
 */
class HistoryActionsFragment :CoroutineFragment(){

    private val datas = ArrayList<StockAction>()
    private val adapter = HistoryActionListAdapter(datas)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_actions,container,false)
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
        getDatas()
        EventBusUtils.register(this)
    }

    private fun getDatas(){
        launch {
            datas.clear();
            datas.addAll(StockDao.getStockActions())
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