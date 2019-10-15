package com.lc.stockhelper.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.stockChart.data.KLineDataManage
import com.lc.stockhelper.R
import com.lc.stockhelper.ui.base.CoroutineFragment
import kotlinx.android.synthetic.main.fragment_kline.*
import org.json.JSONObject


/**
 * K线
 */
class ChartKLineFragment : CoroutineFragment() {

    private var land: Boolean = false//是否横屏
    private var kLineData: KLineDataManage? = null

    private lateinit var data: String
    private var type: Int = TYPE_DAY
    protected fun initBase(view: View) {
        kLineData = KLineDataManage(activity as Context)
        combinedchart!!.initChart(land)

        //上证指数代码000001.IDX.SH
        kLineData!!.parseKlineData(JSONObject(data), "000001.IDX.SH", land)
        combinedchart!!.setDataToChart(kLineData)

        combinedchart!!.getGestureListenerCandle().setCoupleClick {
            if (!land) {
                onClickListener?.onClick(combinedchart)
            }
        }

        combinedchart!!.getGestureListenerBar().setCoupleClick {
            if (land) {

            } else {
                onClickListener?.onClick(combinedchart)
            }
        }
    }


    var onClickListener:View.OnClickListener?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        land = arguments!!.getBoolean(KEY_LANDSCAPE)
        data = arguments!!.getString(KEY_DATA)
        type = arguments!!.getInt(KEY_TYPE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBase(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {

        const val KEY_DATA = "key_data"
        const val KEY_LANDSCAPE = "key_landscape"
        const val KEY_TYPE = "key_type"

        const val TYPE_DAY = 1
        const val TYPE_WEEK = 2
        fun newInstance(type: Int, data: String, land: Boolean): ChartKLineFragment {
            val fragment = ChartKLineFragment()
            val bundle = Bundle()
            bundle.putString(KEY_DATA, data)
            bundle.putBoolean(KEY_LANDSCAPE, land)
            bundle.putInt(KEY_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }
}
