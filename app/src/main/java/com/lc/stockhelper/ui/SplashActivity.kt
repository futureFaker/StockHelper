package com.lc.stockhelper.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar
import com.lc.stockhelper.R
import com.yasic.library.particletextview.MovingStrategy.CornerStrategy
import com.yasic.library.particletextview.Object.ParticleTextViewConfig
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by KID on 2019-09-20.
 */
class SplashActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        ImmersionBar.with(this)
            .transparentBar()
            .fitsSystemWindows(true)
            .init()
        val config = ParticleTextViewConfig.Builder()
            .setRowStep(6)
            .setColumnStep(6)
            .setTargetText("巴飞策选股")
            .setReleasing(0.2)
            .setParticleRadius(2f)
            .setMiniDistance(0.1)
            .setTextSize(150)
            .setMovingStrategy(CornerStrategy())
            .setParticleColorArray(arrayOf("#FFFFFFFF"))
            .setDelay(-1L)
            .instance()

        particleTextView.setConfig(config)
        particleTextView.startAnimation()

        GlobalScope.launch{
            delay(1500)
            var intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}