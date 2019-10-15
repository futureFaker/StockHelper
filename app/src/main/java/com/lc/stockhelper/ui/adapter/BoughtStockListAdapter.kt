package com.lc.stockhelper.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lc.stockhelper.R
import com.lc.stockhelper.data.BoughtStock
import com.lc.stockhelper.utils.getYYYYMMDDHHMMSSFromTimeStamp
import kotlinx.android.synthetic.main.item_bought_stock.view.*
/**
 * Created by KID on 2019-09-30.
 */
class BoughtStockListAdapter(
    val datas: List<BoughtStock>
) : RecyclerView.Adapter<BoughtStockListAdapter.ViewHolder>() {


    interface OnItemActionListener {
        fun onItemClick(view: View, boughtStock: BoughtStock)

        fun onItemBoughtClick(position: Int, boughtStock: BoughtStock)
    }

    var onItemActionListener: OnItemActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_bought_stock,
                parent,
                false
            ), onItemActionListener
        )
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(position, datas.get(position))
    }

    class ViewHolder(
        itemView: View,
        val onItemActionListener: OnItemActionListener?
    ) :
        RecyclerView.ViewHolder(itemView) {

        fun setData(position: Int, boughtStock: BoughtStock) {
            itemView.stockCode.text = boughtStock.stock.code
            itemView.stockName.text = boughtStock.stock.name
            itemView.stockType.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    boughtStock.stock.getTypeColor()
                )
            )
            itemView.stockType.text = boughtStock.stock.type.toString()

            itemView.setOnClickListener {
                onItemActionListener?.onItemClick(
                    itemView,
                    boughtStock
                )
            }

            itemView.stockBought.isSelected = boughtStock.stock.bought
            itemView.stockBought.setOnClickListener {
                onItemActionListener?.onItemBoughtClick(position, boughtStock)
            }

            itemView.stockPrice.text = "￥${boughtStock.price}"
            itemView.actionTime.text = getYYYYMMDDHHMMSSFromTimeStamp(boughtStock.time)

        }

    }

}