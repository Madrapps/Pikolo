package com.madrapps.pikolo

import android.content.Context
import android.graphics.*
import android.support.v4.graphics.ColorUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View


class ColorPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private lateinit var hueShader: Shader
    private var hueIndicatorX = 0f
    private var hueIndicatorY = 0f
    private var hueAngle = 0.0
    private var hueIndicatorSelected = false

    private val shaderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private lateinit var saturationShader: Shader
    private var saturationIndicatorX = 0f
    private var saturationIndicatorY = 0f
    private var saturationAngle = 240.0
    private var saturationIndicatorSelected = false

    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val indicatorRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)
    private val strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f


    private var innerRadius = 0f
    private lateinit var innerCircleArcReference: RectF

    override fun onDraw(canvas: Canvas) {
        drawHueCircle(canvas)
        drawHueIndicator(canvas)

        drawSaturationArc(canvas)
        drawSaturationIndicator(canvas)
    }

    private fun drawSaturationArc(canvas: Canvas) {
        shaderPaint.shader = saturationShader
        canvas.drawArc(innerCircleArcReference, 105f, 150f, false, shaderPaint)
    }

    private fun drawSaturationIndicator(canvas: Canvas) {
        saturationIndicatorX = (centerX + innerRadius * Math.cos(Math.toRadians(saturationAngle))).toFloat()
        saturationIndicatorY = (centerY + innerRadius * Math.sin(Math.toRadians(saturationAngle))).toFloat()

        indicatorPaint.style = Paint.Style.FILL
        indicatorPaint.color = ColorUtils.HSLToColor(floatArrayOf(hueAngle.toFloat(), getSaturation(saturationAngle), 0.5f))
        canvas.drawCircle(saturationIndicatorX, saturationIndicatorY, indicatorRadius, indicatorPaint)

        indicatorPaint.style = Paint.Style.STROKE
        indicatorPaint.strokeWidth = 5f
        indicatorPaint.color = Color.WHITE
        canvas.drawCircle(saturationIndicatorX, saturationIndicatorY, indicatorRadius, indicatorPaint)
    }

    private fun getSaturation(saturationAngle: Double): Float {
        val baseAngle = saturationAngle - 105.0
        Log.d("ColorPicker", "sAngle=$saturationAngle, bAngle=$baseAngle, sat=${(baseAngle / 150f).toFloat()}")
        return (baseAngle / 150f).toFloat()
    }

    private fun drawHueCircle(canvas: Canvas) {
        shaderPaint.shader = hueShader

        shaderPaint.style = Paint.Style.STROKE
        shaderPaint.strokeWidth = strokeWidth

        shaderPaint.strokeCap = Paint.Cap.ROUND
        canvas.drawCircle(centerX, centerY, radius, shaderPaint)
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

        onHueChanged(0.0)

        innerRadius = radius - 80f
        innerCircleArcReference = RectF(centerX - innerRadius, centerY - innerRadius, centerX + innerRadius, centerY + innerRadius)
    }

    private fun getSaturationColorArray(color: Int): IntArray {
        val range = 0..10
        val colors = IntArray(range.count())

        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(color, hsl)

        for (i in range) {
            hsl[1] = i.toFloat() / 10f
            colors[i] = ColorUtils.HSLToColor(hsl)
        }
        return colors
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
                when {
                    isHueIndicatorSelected(x, y) -> {
                        hueIndicatorSelected = true
                        calculateHueAngle(x, y)
                    }
                    isSaturationIndicatorSelected(x, y) -> {
                        saturationIndicatorSelected = true
                        calculateSaturationAngle(x, y)
                    }
                }
            }

            ACTION_MOVE -> {
                when {
                    hueIndicatorSelected -> calculateHueAngle(x, y)
                    saturationIndicatorSelected -> calculateSaturationAngle(x, y)
                }
            }

            ACTION_UP -> {
                hueIndicatorSelected = false
                saturationIndicatorSelected = false
            }
        }

        invalidate()
        return true
    }

    private fun isHueIndicatorSelected(x: Int, y: Int) = x in (hueIndicatorX - indicatorRadius)..(hueIndicatorX + indicatorRadius) && y in (hueIndicatorY - indicatorRadius)..(hueIndicatorY + indicatorRadius)
    private fun isSaturationIndicatorSelected(x: Int, y: Int) = x in (saturationIndicatorX - indicatorRadius)..(saturationIndicatorX + indicatorRadius) && y in (saturationIndicatorY - indicatorRadius)..(saturationIndicatorY + indicatorRadius)

    private fun calculateSaturationAngle(x1: Int, y1: Int) {
        val x = x1 - centerX
        val y = y1 - centerY
        val c = Math.sqrt((x * x + y * y).toDouble())

        var angle = Math.toDegrees(Math.acos(x / c))
        if (y < 0) {
            angle = 360 - angle
        }

        // limit the angle between 105 to 105+150
        if (angle < 105) {
            angle = 105.0
        } else if (angle > 105 + 150) {
            angle = 105.0 + 150.0
        }

        saturationAngle = angle
    }

    private fun calculateHueAngle(x1: Int, y1: Int) {
        val x = x1 - centerX
        val y = y1 - centerY
        val c = Math.sqrt((x * x + y * y).toDouble())

        hueAngle = Math.toDegrees(Math.acos(x / c))
        if (y < 0) {
            hueAngle = 360 - hueAngle
        }

        onHueChanged(hueAngle)
    }

    private fun onHueChanged(hue: Double) {
        val color = Color.HSVToColor(floatArrayOf(hue.toFloat(), 1f, 1f))
        val colors = getSaturationColorArray(color)

        val position = floatArrayOf(0f, 15f / 360f, 30f / 360f, 45f / 360f, 60f / 360f, 75f / 360f, 90f / 360f, 105f / 360f, 120f / 360f, 135f / 360f, 150f / 360f)
        val saturationMatrix = Matrix()
        saturationMatrix.setRotate(100f, centerX, centerY) // TODO This 100 should be calculated based on the stroke width
        saturationShader = SweepGradient(centerX, centerY, colors, position)
        saturationShader.setLocalMatrix(saturationMatrix)
    }

}