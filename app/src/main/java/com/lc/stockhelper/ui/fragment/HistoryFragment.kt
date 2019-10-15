package com.lc.stockhelper.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lc.stockhelper.R
import com.lc.stockhelper.ui.adapter.SimpleFragmentPagerAdapter
import com.lc.stockhelper.ui.base.CoroutineFragment
import kotlinx.android.synthetic.main.fragment_history.*

/**
 * Created by KID on 2019-10-08.
 */
class HistoryFragment :CoroutineFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragments = arrayOf<Fragment>(
            HistoryStocksFragment(),
            HistoryActionsFragment()
        )
        val titles = arrayOf("股票","操作")
        viewPager.setOffscreenPageLimit(fragments.size)
        viewPager.setAdapter(
            SimpleFragmentPagerAdapter(
                childFragmentManager,
                fragments,
                titles
            )
        )
        tabLayout.setupWithViewPager(viewPager)
    }

}