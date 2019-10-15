package com.lc.stockhelper.data.server

import com.alibaba.fastjson.JSON
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.lc.stockhelper.data.Stock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by KID on 2019-09-30.
 */

class StockServerApi {
    companion object {
        const val TOADY_STOCK_LIST_URL = "http://www.ymhd.xyz/getStockList"
        const val STOCK_DETAILS_URL = "http://www.ymhd.xyz/getStockData?"

        suspend fun getTodayStockList(): List<Stock> = withContext(Dispatchers.IO) {

            val (_, _, result) = TOADY_STOCK_LIST_URL.httpGet().responseString()
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    println("getTodayStockList ："+ex)
                }

                is Result.Success -> {
                    val data = result.get()
                    println("getTodayStockList ："+data)
                    val array = JSON.parseArray(data, Stock::class.java)
                    return@withContext array
                }
            }

            return@withContext ArrayList<Stock>()
        }

        suspend fun getStockDetails(stock: Stock):String = withContext(Dispatchers.IO) {
            val (_, _, result) = (STOCK_DETAILS_URL + "code=${stock.code}&num=100").httpGet().responseString()
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    println("getStockDetails ："+ex)
                }

                is Result.Success -> {
                    val data = result.get()

                    println("getStockDetails ："+data)

                    return@withContext data
                }
            }

            return@withContext ""
        }

        suspend fun getStockWeekDetails(stock: Stock):String = withContext(Dispatchers.IO) {
            val (_, _, result) = (STOCK_DETAILS_URL + "code=${stock.code}&num=100&type=week").also {
                println(it)
            }.httpGet().responseString()
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    println("getStockWeekDetails ："+ex)
                }

                is Result.Success -> {
                    val data = result.get()
                    println("getStockWeekDetails ："+data)

                    return@withContext data
                }
            }

            return@withContext ""
        }
    }
}

