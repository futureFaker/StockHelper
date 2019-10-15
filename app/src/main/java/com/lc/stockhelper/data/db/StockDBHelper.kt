package com.lc.stockhelper.data.db

import android.database.sqlite.SQLiteDatabase
import com.lc.stockhelper.App
import com.lc.stockhelper.data.Stock
import org.jetbrains.anko.db.INTEGER
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable
/**
 * Created by KID on 2019-10-08.
 */
class StockDBHelper(context: App = App.instance) : ManagedSQLiteOpenHelper(
    context, DB_NAME, null,
    DB_VERSION
) {

    init {
        createTodayTable(writableDatabase)
    }

    companion object {
        const val DB_NAME = "stock.db"
        const val DB_VERSION = 1
        val instance by lazy { StockDBHelper() }
    }

    override fun onCreate(db: SQLiteDatabase) {
        createTodayTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        createTodayTable(db)
    }

    private fun createTodayTable(db: SQLiteDatabase){
        db.createTable(
            Stock.StaticO.getTodayTableName(), true,
            Stock.StaticO.TYPE to INTEGER,
            Stock.StaticO.NAME to TEXT,
            Stock.StaticO.CODE to TEXT
        )

        db.createTable(
            Stock.StaticO.getSelectedTableName(), true,
            Stock.StaticO.TYPE to INTEGER,
            Stock.StaticO.NAME to TEXT,
            Stock.StaticO.CODE to TEXT
        )

        db.createTable(
            Stock.StaticO.getBoughtTableName(),true,
            Stock.StaticO.TYPE to INTEGER,
            Stock.StaticO.NAME to TEXT,
            Stock.StaticO.CODE to TEXT,
            Stock.StaticO.TIME to TEXT,
            Stock.StaticO.PRICE to TEXT
        )

        db.createTable(
            Stock.StaticO.getActionTableName(),true,
            Stock.StaticO.TYPE to INTEGER,
            Stock.StaticO.NAME to TEXT,
            Stock.StaticO.CODE to TEXT,
            Stock.StaticO.TIME to TEXT,
            Stock.StaticO.PRICE to TEXT,
            Stock.StaticO.ACTION to INTEGER
        )
    }
}