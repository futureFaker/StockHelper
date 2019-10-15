package com.lc.stockhelper.utils

import android.app.Activity
import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input

/**
 * Created by KID on 2019-08-26.
 */
class DialogUtils {

    //伴生对象
    companion object {

        fun showSellStockDialog(activity: Activity, onPostiveListener: (price: Double) -> Unit) {
            MaterialDialog(activity).show {
                title(text = "卖出股票")
                input(
                    hint = "请输入卖出股票价格",
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                ) { dialog, text ->
                    onPostiveListener.invoke(text.toString().toDouble())
                }
                positiveButton(text = "提交")
            }
        }

        fun showBuyStockDialog(activity: Activity, onPostiveListener: (price: Double) -> Unit) {

            MaterialDialog(activity).show {
                title(text = "买入股票")
                input(
                    hint = "请输入买入股票的价格",
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                ) { dialog, text ->
                    onPostiveListener.invoke(text.toString().toDouble())
                }
                positiveButton(text = "提交")
            }
        }

    }


}
