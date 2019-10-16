package com.lc.stockhelper.ui.widget.stockChart.data;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;

/**
 * Created by KID on 2019-10-11.
 */
public class DataUtils {

    public static long getTimeStamp(String time){
        long timeStamp = (new SimpleDateFormat("yyyyMMdd")).parse(time, new ParsePosition(0)).getTime();
        return timeStamp;
    }

}
