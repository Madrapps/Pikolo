package com.madrapps.pikolo.components

import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.Shader
import android.view.MotionEvent
import android.view.MotionEvent.*
import com.madrapps.pikolo.Metrics
import com.madrapps.pikolo.Paints
import com.madrapps.pikolo.listeners.OnColorSelectionListener

internal abstract class ColorComponent(val metrics: Metrics, val paints: Paints) {

    var radius: Float = 0f

    var fillWidth: Float = 0f
    var strokeWidth: Float = 0f
    var strokeColor: Int = 0

    var indicatorRadius: Float = 0f
    var indicatorStrokeWidth: Float = 0f
    var indicatorStrokeColor: Int = 0

    var indicatorX: Float = 0f
    var indicatorY: Float = 0f

    var angle: Double = 0.0

    private var isTouched = false
    private var colorSelectionListener: OnColorSelectionListener? = null

    abstract fun getShader(): Shader
    abstract fun drawComponent(canvas: Canvas)

    fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            ACTION_DOWN -> {
                if (PointF(x, y) in this) {
                    colorSelectionListener?.onColorSelectionStart(metrics.getColor())
                    isTouched = true
                    calculateAngle(x, y)
                    updateComponent(angle)
                    colorSelectionListener?.onColorSelected(metrics.getColor())
                }
            }

            ACTION_MOVE -> {
                if (isTouched) {
                    calculateAngle(x, y)
                    updateComponent(angle)
                    colorSelectionListener?.onColorSelected(metrics.getColor())
                }
            }

            ACTION_UP -> {
                if (isTouched) colorSelectionListener?.onColorSelectionEnd(metrics.getColor())
                isTouched = false
            }
        }

        return isTouched
    }

    operator fun contains(point: PointF): Boolean {
        val touchRadius = indicatorRadius + indicatorRadius * 0.2
        return point.x in (indicatorX - touchRadius)..(indicatorX + touchRadius) && point.y in (indicatorY - touchRadius)..(indicatorY + touchRadius)
    }

    open fun calculateAngle(x1: Float, y1: Float) {
        val x = x1 - metrics.centerX
        val y = y1 - metrics.centerY
        val c = Math.sqrt((x * x + y * y).toDouble())

        angle = Math.toDegrees(Math.acos(x / c))
        if (y < 0) {
            angle = 360 - angle
        }
    }

    abstract fun updateComponent(angle: Double)

    abstract fun updateAngle(component: Float)

    internal fun setColorSelectionListener(listener: OnColorSelectionListener) {
        colorSelectionListener = listener
    }

    internal fun setRadius(outerRadius: Float, offset: Float) {
        radius = outerRadius - (Math.max(indicatorRadius + indicatorStrokeWidth, fillWidth)) - offset
    }
}