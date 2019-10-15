package com.lc.stockhelper.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lc.stockhelper.R
import com.lc.stockhelper.data.Stock
import kotlinx.android.synthetic.main.item_stock.view.*

/**
 * Created by KID on 2019-09-30.
 */
class StockListAdapter(
    val datas: List<Stock>
) : RecyclerView.Adapter<StockListAdapter.ViewHolder>() {

    var showAction = true
        set(value) {
            field = value
        }

    interface OnItemActionListener {
        fun onItemClick(view: View, stock: Stock)

        fun onItemSelectedClick(position: Int, stock: Stock)

        fun onItemBoughtClick(position: Int, stock: Stock)
    }

    var onItemActionListener: OnItemActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_stock,
                parent,
                false
            ), onItemActionListener, showAction
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
        val onItemActionListener: OnItemActionListener?,
        val showAction: Boolean
    ) :
        RecyclerView.ViewHolder(itemView) {

        fun setData(position: Int, stock: Stock) {
            itemView.stockCode.text = stock.code
            itemView.stockName.text = stock.name
            itemView.stockType.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    stock.getTypeColor()
                )
            )
            itemView.stockType.text = stock.type.toString()

            if(showAction){
                itemView.setOnClickListener { onItemActionListener?.onItemClick(itemView, stock) }
                itemView.stockSelected.isSelected = stock.selected
                itemView.stockSelected.setOnClickListener {
                    onItemActionListener?.onItemSelectedClick(position, stock)
                }

                itemView.stockBought.isSelected = stock.bought
                itemView.stockBought.setOnClickListener {
                    onItemActionListener?.onItemBoughtClick(position, stock)
                }
            }else{
                itemView.stockSelected.visibility = View.GONE
                itemView.stockBought.visibility = View.GONE
            }

        }

    }

}