package com.fphoenixcorneae.widget.rocker

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.*

/**
 * @desc：摇杆改变监听
 * @date：2022/11/09 17:21
 */
typealias OnSteeringWheelChangedListener = (
    @ParameterName("linearSpeed") Float,
    @ParameterName("angleSpeed") Float,
) -> Unit

/**
 * @desc：摇杆
 * @date：2022/11/09 17:22
 */
class RockerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var defaultColor = DEFAULT_COLOR
    private var touchedColor = TOUCHED_COLOR
    private val path = Path()
    private val paint = Paint().apply {
        isAntiAlias = true
        alpha = 0x88
        color = defaultColor
    }
    private var outerCenterX = 0f
    private var outerCenterY = 0f
    private var innerCenterX = 0f
    private var innerCenterY = 0f
    private var outerR = 0f
    private var innerR = 0f
    private var isRockerTouched = false
    private var degree = 0.0

    /** 事件回调接口 */
    private var onSteeringWheelChangedListener: OnSteeringWheelChangedListener? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        outerR = w / 3f
        innerR = w / 6f
        outerCenterX = w / 2f
        outerCenterY = h / 2f
        innerCenterX = outerCenterX
        innerCenterY = outerCenterX
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (measuredWidth == 0 || measuredHeight == 0) {
            return
        }
        drawSelf(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val currentX = event.x
        val currentY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                begin(currentX, currentY)
            }
            MotionEvent.ACTION_MOVE -> {
                update(currentX, currentY)
            }
            MotionEvent.ACTION_UP -> {
                parent.requestDisallowInterceptTouchEvent(false)
                reset()
            }
            else -> {}
        }
        return true
    }

    private fun drawSelf(canvas: Canvas) {
        paint.maskFilter = if (innerCenterX != outerCenterX || innerCenterY != outerCenterY) {
            BlurMaskFilter(30f, BlurMaskFilter.Blur.SOLID)
        } else {
            null
        }
        path.reset()
        path.addCircle(outerCenterX, outerCenterY, width / 2f, Path.Direction.CW)
        canvas.clipPath(path)
        canvas.drawCircle(innerCenterX, innerCenterY, innerR, paint)
        if (onSteeringWheelChangedListener != null) {
            // 计算两点之间距离
            val innerDistance = distance(innerCenterX, innerCenterY, outerCenterX, outerCenterY)
            val r0 = (innerDistance / outerR) * MAX_THRESHOLD
            // 获取水平线夹角弧度
            val radian = getRadian(outerCenterX, outerCenterY, innerCenterX, innerCenterY)
            // 获取摇杆偏移角度
            val angle = getAngleConvert(radian)
            // 算出弧度
            val angleTemp = Math.toRadians(angle)
            // 线速度
            val linearSpeed = floor(r0 * sin(angleTemp) + 0.5f).toInt().toFloat()
            // 角度乘以速度等于向心加速度（角速度）
            val angleSpeed = -floor(r0 * cos(angleTemp) + 0.5f).toInt().toFloat()
            onSteeringWheelChangedListener?.invoke(linearSpeed, angleSpeed)
        }
    }

    /**
     * 获取摇杆偏移角度 0-360°
     */
    private fun getAngleConvert(radian: Double): Double {
        val tmp = Math.toDegrees(radian)
        return if (tmp < 0) {
            -tmp
        } else {
            360 - tmp
        }
    }

    /**
     * 获取水平线夹角弧度
     */
    private fun getRadian(x1: Float, y1: Float, x2: Float, y2: Float): Double {
        val lenA = x2 - x1
        val lenB = y2 - y1
        val lenC = sqrt((lenA * lenA + lenB * lenB).toDouble())
        var ang = acos(lenA / lenC)
        ang *= if (y2 < y1) -1 else 1
        return ang
    }

    /**
     * Call this method when ACTION_DOWN
     */
    private fun begin(x: Float, y: Float) {
        paint.color = touchedColor
        val distance = distance(x, y, outerCenterX, outerCenterY)
        if (distance < outerR) {
            // 滑动点和原地的距离大于外半径的时候做保护，如果在范围内表示触摸成功
            innerCenterX = x
            innerCenterY = y
            isRockerTouched = true
        }
        // 表示坐标的角度
        degree = atan(((x - outerCenterX) / (y - outerCenterY)).toDouble())
        postInvalidate()
    }

    /**
     * Call this method when ACTION_MOVE
     */
    private fun update(x: Float, y: Float) {
        if (isRockerTouched) {
            val distance = distance(x, y, outerCenterX, outerCenterY)
            degree = atan(((x - outerCenterX) / (y - outerCenterY)).toDouble())
            if (distance < outerR) {
                innerCenterX = x
                innerCenterY = y
            } else if (y < outerCenterY) {
                innerCenterX = outerCenterX + (outerR * -sin(degree)).toFloat()
                innerCenterY = outerCenterY + (outerR * -cos(degree)).toFloat()
            } else {
                innerCenterX = outerCenterX + (outerR * sin(degree)).toFloat()
                innerCenterY = outerCenterY + (outerR * cos(degree)).toFloat()
            }
        }
        postInvalidate()
    }

    /**
     * Call this method when ACTION_UP
     */
    private fun reset() {
        paint.color = defaultColor
        innerCenterX = outerCenterX
        innerCenterY = outerCenterY
        isRockerTouched = false
        degree = 0.0
        postInvalidate()
    }

    /**
     * Calculate the distance between two dots
     */
    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt(((x1 - x2).toDouble().pow(2.0).toFloat() + (y1 - y2).toDouble().pow(2.0)
            .toFloat()).toDouble()).toFloat()
    }

    /**
     * 设置摇杆改变监听
     */
    fun setOnSteeringWheelChangedListener(listener: OnSteeringWheelChangedListener?) = run {
        this.onSteeringWheelChangedListener = listener
    }

    /**
     * 设置默认状态下的摇杆颜色
     */
    fun setDefaultColor(@ColorInt color: Int) = apply {
        defaultColor = color
        paint.color = defaultColor
    }

    /**
     * 设置按下状态下的摇杆颜色
     */
    fun setTouchedColor(@ColorInt color: Int) = apply {
        touchedColor = color
    }

    /**
     * 设置背景着色
     */
    fun setBackgroundTint(@ColorInt color: Int) = apply {
        backgroundTintList = ColorStateList.valueOf(color)
    }

    init {
        background = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(Color.parseColor("#99010a15"))
        }
    }

    companion object {
        private const val TOUCHED_COLOR = Color.YELLOW
        private const val DEFAULT_COLOR = Color.WHITE

        /** 最大阈值 */
        private const val MAX_THRESHOLD = 80f
    }
}