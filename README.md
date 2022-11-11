# RockerView
摇杆

[![](https://jitpack.io/v/FPhoenixCorneaE/RockerView.svg)](https://jitpack.io/#FPhoenixCorneaE/RockerView)

<p align="center"> <img src="https://github.com/FPhoenixCorneaE/RockerView/blob/main/image/rocker_view.png" alt="预览图片"  width="320"></p>


How to include it in your project:
--------------
**Step 1.** Add the JitPack repository to your build file
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency
```groovy
dependencies {
	implementation("com.github.FPhoenixCorneaE:RockerView:$latest")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
}
```

代码中使用
-------------------
```kotlin
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
```
