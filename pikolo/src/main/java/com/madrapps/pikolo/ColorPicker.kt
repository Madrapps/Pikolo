package com.madrapps.pikolo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View


class ColorPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private lateinit var hueShader: Shader
    private val huePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var hueIndicatorX = 0f
    private var hueIndicatorY = 0f
    private var hueAngle = 0.0

    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var indicatorSelected: Boolean = false
    private val indicatorRadius = 50f
    private val strokeWidth = 40f
    private var radius: Float = 0f
    private var centerX = 0f
    private var centerY = 0f

    override fun onDraw(canvas: Canvas) {
        drawHueCircle(canvas)
        drawHueIndicator(canvas)
    }

    private fun drawHueCircle(canvas: Canvas) {
        huePaint.style = Paint.Style.STROKE
        huePaint.strokeWidth = strokeWidth
        canvas.drawCircle(centerX, centerY, radius, huePaint)
    }

    private fun drawHueIndicator(canvas: Canvas) {
        hueIndicatorX = (centerX + radius * Math.cos(Math.toRadians(hueAngle))).toFloat()
        hueIndicatorY = (centerY + radius * Math.sin(Math.toRadians(hueAngle))).toFloat()

        indicatorPaint.style = Paint.Style.FILL
        indicatorPaint.color = Color.HSVToColor(floatArrayOf(hueAngle.toFloat(), 1f, 1f))
        canvas.drawCircle(hueIndicatorX, hueIndicatorY, indicatorRadius, indicatorPaint)

        indicatorPaint.style = Paint.Style.STROKE
        indicatorPaint.strokeWidth = 5f
        indicatorPaint.color = Color.WHITE
        canvas.drawCircle(hueIndicatorX, hueIndicatorY, indicatorRadius, indicatorPaint)
    }

    override fun onSizeChanged(width: Int, height: Int, oldW: Int, oldH: Int) {
        val minimumSize = if (width > height) height else width

        val padding = (paddingLeft + paddingRight + paddingTop + paddingBottom) / 4f
        radius = minimumSize.toFloat() / 2f - padding - strokeWidth - indicatorRadius

        centerX = width / 2f
        centerY = height / 2f

        hueShader = SweepGradient(centerX, centerY, getColorArray(), null)
        huePaint.shader = hueShader
    }

    private fun getColorArray(): IntArray {
        val colors = IntArray(360)
        val hsv = floatArrayOf(0f, 1f, 1f)
        for (i in 0..359) {
            hsv[0] = i.toFloat()
            colors[i] = Color.HSVToColor(hsv)
        }
        return colors
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            ACTION_DOWN -> {
                if (x in (hueIndicatorX - indicatorRadius)..(hueIndicatorX + indicatorRadius) && y in (hueIndicatorY - indicatorRadius)..(hueIndicatorY + indicatorRadius)) {
                    indicatorSelected = true
                    calculateAngle(x, y)
                }
            }

            ACTION_MOVE -> {
                if (indicatorSelected) {
                    calculateAngle(x, y)
                }
            }

            ACTION_UP -> {
                indicatorSelected = false
            }
        }

        invalidate()
        return true
    }

    private fun calculateAngle(x1: Int, y1: Int) {
        val x = x1 - centerX
        val y = y1 - centerY
        val c = Math.sqrt((x * x + y * y).toDouble())

        hueAngle = Math.toDegrees(Math.acos(x / c))
        if (y < 0) {
            hueAngle = 360 - hueAngle
        }
    }

}