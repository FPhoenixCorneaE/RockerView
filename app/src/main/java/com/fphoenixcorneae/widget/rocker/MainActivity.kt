package com.fphoenixcorneae.widget.rocker

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 * @desc：
 * @date：2022/11/11 10:59
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RockerView>(R.id.ivRocker)
            // 设置默认状态下的摇杆颜色
            .setDefaultColor(Color.MAGENTA)
            // 设置按下状态下的摇杆颜色
            .setTouchedColor(Color.BLUE)
            // 设置背景着色
            .setBackgroundTint(Color.CYAN)
            // 设置摇杆改变监听
            .setOnSteeringWheelChangedListener { linearSpeed, angleSpeed ->
                Log.i("RockerView", "linearSpeed: $linearSpeed, angleSpeed: $angleSpeed")
            }
    }
}