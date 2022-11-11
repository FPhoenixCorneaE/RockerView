package com.fphoenixcorneae.widget.rocker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RockerView>(R.id.ivRocker)
            .setOnSteeringWheelChangedListener { linearSpeed, angleSpeed ->
                Log.i("RockerView", "linearSpeed: $linearSpeed, angleSpeed: $angleSpeed")
            }
    }
}