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
            .setDefaultColor(Color.MAGENTA)
            .setTouchedColor(Color.BLUE)
            .setBackgroundTint(Color.CYAN)
            .setOnSteeringWheelChangedListener { linearSpeed, angleSpeed ->
                Log.i("RockerView", "linearSpeed: $linearSpeed, angleSpeed: $angleSpeed")
            }
    }
}