package com.lc.stockhelper.data.db

import com.lc.stockhelper.data.BoughtStock
import com.lc.stockhelper.data.Stock
import com.lc.stockhelper.data.StockAction
import com.lc.stockhelper.data.event.SelectedStockChangeEvent
import com.lc.stockhelper.data.event.StockActionEvent
import com.lc.stockhelper.utils.EventBusUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.anko.db.*

/**
 * Created by KID on 2019-10-08.
 */
class StockDao {
    companion object {
        suspend fun addTodayStocks(stocks: List<Stock>) = withContext(Dispatchers.IO) {
            StockDBHelper.instance.use {
                stocks.forEach {
                    insert(
                        Stock.StaticO.getTodayTableName(),
                        Stock.StaticO.TYPE to it.type,
                        Stock.StaticO.NAME to it.name,
                        Stock.StaticO.CODE to it.code
                    )
                }
            }
        }

        suspend fun removeTodayStocks() = withContext(Dispatchers.IO) {
            StockDBHelper.instance.use {
                delete(Stock.StaticO.getTodayTableName())
            }
        }

        suspend fun getTodayStocks(): List<Stock> = withContext(Dispatchers.IO) {
            StockDBHelper.instance.use {
                select(Stock.StaticO.getTodayTableName())
                    .exec {
                        parseList(classParser<Stock>())
                    }

            }
        }

        suspend fun addSelectedStock(stock: Stock) = withContext(Dispatchers.IO) {
            StockDBHelper.instance.use {
                insert(
                    Stock.StaticO.getSelectedTableName(),
                    Stock.StaticO.TYPE to stock.type,
                    Stock.StaticO.NAME to stock.name,
                    Stock.StaticO.CODE to stock.code
                )
                EventBusUtils.post(SelectedStockChangeEvent())
            }
        }

        suspend fun removeSelectedStock(stock: Stock) = withContext(Dispatchers.IO) {
            StockDBHelper.instance.use {
                delete(
                    Stock.StaticO.getSelectedTableName(),
                    "${Stock.StaticO.CODE} = ?",
                    arrayOf(stock.code)
                )
                EventBusUtils.post(SelectedStockChangeEvent())
            }
        }

        suspend fun getSelectedStocks(): List<Stock> = withContext(Dispatchers.IO) {
            StockDBHelper.instance.use {
                select(Stock.StaticO.getSelectedTableName())
                    .exec {
                        parseList(classParser<Stock>()).apply {
                            map {
                                it.selected = true
                            }
                        }
                    }
            }
        }

        suspend fun addBoughtStock(stock: Stock, price: Double) = withContext(Dispatchers.IO) {
            StockDBHelper.instance.use {
                val time = System.currentTimeMillis()
                insert(
                    Stock.StaticO.getBoughtTableName(),
                    Stock.StaticO.TYPE to stock.type,
                    Stock.StaticO.NAME to stock.name,
                    Stock.StaticO.CODE to stock.code,
                    Stock.StaticO.PRICE to price,
                    Stock.StaticO.TIME to time.toString()
                )
                insert(
                    Stock.StaticO.getActionTableName(),
                    Stock.StaticO.TYPE to stock.type,
                    Stock.StaticO.NAME to stock.name,
                    Stock.StaticO.CODE to stock.code,
                    Stock.StaticO.PRICE to price,
                    Stock.StaticO.TIME to time.toString(),
                    Stock.StaticO.ACTION to Stock.StaticO.ACTION_BUY
                )
                EventBusUtils.post(StockActionEvent())
                EventBusUtils.post(SelectedStockChangeEvent())
            }
        }

        suspend fun removeBoughtStock(stock: Stock, price: Double) = withContext(Dispatchers.IO) {
            StockDBHelper.instance.use {
                delete(
                    Stock.StaticO.getBoughtTableName(),
                    "${Stock.StaticO.CODE} = ?",
                    arrayOf(stock.code)
                )
                insert(
                    Stock.StaticO.getActionTableName(),
                    Stock.StaticO.TYPE to stock.type,
                    Stock.StaticO.NAME to stock.name,
                    Stock.StaticO.CODE to stock.code,
                    Stock.StaticO.PRICE to price,
                    Stock.StaticO.TIME to System.currentTimeMillis().toString(),
                    Stock.StaticO.ACTION to Stock.StaticO.ACTION_SALE
                )
                EventBusUtils.post(StockActionEvent())
                EventBusUtils.post(SelectedStockChangeEvent())
            }
        }

        suspend fun getBoughtStocks(): List<BoughtStock> = withContext(Dispatchers.IO) {
            StockDBHelper.instance.use {
                select(Stock.StaticO.getBoughtTableName())
                    .exec {
                        val parse =
                            rowParser { type: Int, name: String, code: String, time: String, price: String ->
                                BoughtStock(
                                    Stock(type, name, code),
                                    price.toDouble(),
                                    time.toLong()
                                )
                            }
                        parseList(parse).apply {
                            map {
                                it.stock.bought = true
                            }
                        }
                    }
            }
        }

        suspend fun getStockActions(): List<StockAction> = withContext(Dispatchers.IO) {
            StockDBHelper.instance.use {
                select(Stock.StaticO.getActionTableName())
                    .exec {
                        val parse =
                            rowParser { type: Int, name: String, code: String, time: String, price: String, action: Int ->
                                StockAction(
                                    BoughtStock(
                                        Stock(type, name, code),
                                        price.toDouble(),
                                        time.toLong()
                                    ), action
                                )
                            }
                        parseList(parse).reversed()
                    }

            }
        }

        suspend fun getStockTableNames(): List<String> = withContext(Dispatchers.IO) {
            StockDBHelper.instance.use {
                select("sqlite_master", "name")
                    .exec {
                        val parse = rowParser { name: String ->
                            name
                        }
                        parseList(parse).filter {
                            it.startsWith(Stock.StaticO.TABLE_NAME)
                                    && !it.equals(Stock.StaticO.getBoughtTableName())
                                    && !it.equals(Stock.StaticO.getActionTableName())
                                    && !it.equals(Stock.StaticO.getSelectedTableName())
                        }.reversed()
                    }
            }
        }

        suspend fun getHistoryStocks(tableName:String): List<Stock> = withContext(Dispatchers.IO) {
            StockDBHelper.instance.use {
                select(tableName)
                    .exec {
                        parseList(classParser<Stock>())
                    }
            }
        }
    }
}

