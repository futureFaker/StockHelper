package com.lc.stockhelper

import android.app.Application
import com.lc.stockhelper.extensions.DelegatesExt
import com.tencent.bugly.Bugly

/**
 * Created by KID on 2019-09-20.
 */
class App : Application(){

    companion object{
        var instance: App by DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Bugly.init(getApplicationContext(), "23921215bb", false);
    }

}