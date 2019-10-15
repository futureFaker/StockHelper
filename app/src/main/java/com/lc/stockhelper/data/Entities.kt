package com.lc.stockhelper.data

import android.os.Parcel
import android.os.Parcelable
import com.lc.stockhelper.R
import com.lc.stockhelper.utils.getTodayTimestamp

/**
 * Created by KID on 2019-09-30.
 */
data class Stock(
    val type: Int,
    val name: String,
    val code: String
) : Parcelable {

    var selected: Boolean = false

    var bought: Boolean = false

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
        selected = parcel.readByte() != 0.toByte()
        bought = parcel.readByte() != 0.toByte()
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeString(name)
        parcel.writeString(code)
        parcel.writeByte(if (selected) 1 else 0)
        parcel.writeByte(if (bought) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Stock> {
        override fun createFromParcel(parcel: Parcel): Stock {
            return Stock(parcel)
        }

        override fun newArray(size: Int): Array<Stock?> {
            return arrayOfNulls(size)
        }
    }

    fun getTypeColor(): Int {
        when (type) {
            1 -> return R.color.colorE06441
            2 -> return R.color.colorF5B133
            3 -> return R.color.colorECF00E
            4 -> return R.color.colorA5ED15
            else -> return R.color.color15EDDF
        }
    }

    object StaticO {
        const val ACTION_BUY = 1
        const val ACTION_SALE = 2

        const val TABLE_NAME = "Stock_"
        const val TYPE = "type"
        const val NAME = "name"
        const val CODE = "code"

        const val TIME = "time"
        const val PRICE = "price"

        const val ACTION = "action"
        fun getTodayTableName(): String {
            return TABLE_NAME + getTodayTimestamp().toString()
        }

        fun getSelectedTableName(): String {
            return TABLE_NAME + "Selected"
        }

        fun getBoughtTableName(): String {
            return TABLE_NAME + "bought"
        }

        fun getActionTableName():String{
            return TABLE_NAME + "action"
        }
    }
}

data class BoughtStock(
    val stock: Stock,
    val price: Double,
    val time: Long
)

data class StockAction(
    val boughtStock: BoughtStock,
    val action:Int
)

data class StockDaily(
    val date: String,
    val openPrice: Double,
    val closePrice: Double,
    val minPrice: Double,
    val maxPrice: Double,
    val change: Double
)





