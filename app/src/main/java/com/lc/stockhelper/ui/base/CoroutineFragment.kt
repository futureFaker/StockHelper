package com.lc.stockhelper.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Created by KID on 2019-09-30.
 */
open class CoroutineFragment : Fragment(),CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    lateinit var job:Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}