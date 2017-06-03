package com.madrapps.pikolo

import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.Shader
import android.view.MotionEvent

abstract class ColorComponent(val metrics: Metrics, val paints: Paints) {

    var radius: Float = 0f
    var strokeWidth: Float = 0f
    var indicatorSize: Float = 0f
    var indicatorX: Float = 0f
    var indicatorY: Float = 0f

    var angle: Double = 0.0

    private var isTouched = false

    abstract fun getShader(): Shader
    abstract fun drawComponent(canvas: Canvas)

    fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (PointF(x, y) in this) {
                    isTouched = true
                    calculateAngle(x, y)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isTouched) calculateAngle(x, y)
            }

            MotionEvent.ACTION_UP -> {
                isTouched = false
            }
        }

        return isTouched
    }

    operator fun contains(point: PointF): Boolean {
        return point.x in (indicatorX - indicatorSize)..(indicatorX + indicatorSize) && point.y in (indicatorY - indicatorSize)..(indicatorY + indicatorSize)
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

}