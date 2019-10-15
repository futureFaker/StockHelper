package com.lc.stockhelper.ui.base

import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.gyf.barlibrary.ImmersionBar
import com.lc.stockhelper.R
import kotlinx.android.synthetic.main.layout_tool_bar.*

/**
 * Created by KID on 2019-09-30.
 */
open abstract class ToolbarActivity : CoroutineActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        ImmersionBar.with(this)
            .statusBarColor(R.color.main_color)
            .fitsSystemWindows(true)
            .init()
    }

    fun setToolbarBackground(color:Int){
        toolBar.setBackgroundColor(ActivityCompat.getColor(this,color))
    }

    fun enableNavigation(){
        toolBar_back.visibility = View.VISIBLE
        toolBar_back.setOnClickListener {
            onBackPressed()
        }
    }

    fun setTitleText(title:String){
        toolBar_title1.text = title
    }

    fun setTitle2Text(title: String){
        toolBar_title2.text = title
    }

    fun setRightIvRes(resId:Int){
        toolBar_right.visibility = View.VISIBLE
        toolBar_right.setImageResource(resId)
    }

    fun setRightIv2Res(resId:Int){
        toolBar_right_2.visibility = View.VISIBLE
        toolBar_right_2.setImageResource(resId)
    }

    fun setRightIvClickListener(onClickListener: (View) -> Unit){
        toolBar_right.setOnClickListener(onClickListener)
    }

    fun setRightIv2ClickListener(onClickListener: (View) -> Unit){
        toolBar_right_2.setOnClickListener(onClickListener)
    }

    abstract fun layoutId():Int
}