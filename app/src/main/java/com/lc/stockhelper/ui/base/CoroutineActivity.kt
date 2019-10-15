package com.lc.stockhelper.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * Created by KID on 2019-09-30.
 */
open class CoroutineActivity : AppCompatActivity(),CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    lateinit var job:Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

}