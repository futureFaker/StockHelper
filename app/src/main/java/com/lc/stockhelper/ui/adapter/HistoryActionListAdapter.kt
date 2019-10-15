package com.lc.stockhelper.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lc.stockhelper.R
import com.lc.stockhelper.data.Stock
import com.lc.stockhelper.data.StockAction
import com.lc.stockhelper.utils.getYYYYMMDDHHMMSSFromTimeStamp
import kotlinx.android.synthetic.main.item_history_action.view.*
/**
 * Created by KID on 2019-10-15.
 */
class HistoryActionListAdapter(val datas: List<StockAction>) :
    RecyclerView.Adapter<HistoryActionListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_history_action,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(datas.get(position))
    }

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun setData(data: StockAction) {
            if(data.action == Stock.StaticO.ACTION_BUY){
                itemView.actionType.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.colorD81E06))
                itemView.actionType.text = "买(￥${data.boughtStock.price})"
            }else{
                itemView.actionType.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.color15ED41))
                itemView.actionType.text = "卖(￥${data.boughtStock.price})"
            }
            itemView.stockName.text = data.boughtStock.stock.name
            itemView.stockCode.text = data.boughtStock.stock.code
            itemView.actionTime.text = getYYYYMMDDHHMMSSFromTimeStamp(data.boughtStock.time)
        }

    }

}