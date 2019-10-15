package com.lc.stockhelper.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lc.stockhelper.R
import com.lc.stockhelper.data.Stock
import com.lc.stockhelper.utils.getYYYYMMDDFromTimeStamp
import kotlinx.android.synthetic.main.item_history_stock.view.*

/**
 * Created by KID on 2019-10-15.
 */
class HistoryStockListAdapter(val datas: List<String>, val onItemClick: (View,String) -> Unit) :
    RecyclerView.Adapter<HistoryStockListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_history_stock,
                parent,
                false
            ), onItemClick
        )
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(datas.get(position))
    }

    class ViewHolder(
        itemView: View,
        val onItemClick: (View,String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        fun setData(data: String) {
            val time = data.replace(Stock.StaticO.TABLE_NAME, "")
            itemView.tv.text = getYYYYMMDDFromTimeStamp(time.toLong())
            itemView.setOnClickListener {
                onItemClick.invoke(itemView,data)
            }
        }

    }

}