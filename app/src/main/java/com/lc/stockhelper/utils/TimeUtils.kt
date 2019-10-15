package com.lc.stockhelper.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by KID on 2019-10-08.
 */

fun getTodayTimestamp(): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    return (calendar.timeInMillis/1000).toInt()
}

fun getYYYYMMDDFromTimeStamp(timeStamp:Long):String{
    val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日")
    return simpleDateFormat.format(timeStamp*1000)
}

fun getYYYYMMDDHHMMSSFromTimeStamp(timeStamp:Long):String{
    val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
    return simpleDateFormat.format(timeStamp)
}
