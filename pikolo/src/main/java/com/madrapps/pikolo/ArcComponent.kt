package com.madrapps.pikolo

import android.graphics.*
import android.support.v4.graphics.ColorUtils
import android.util.Log

abstract class ArcComponent(metrics: Metrics, paints: Paints) : ColorComponent(metrics, paints) {
    private val NO_OF_COLORS = 11

    private val colors = IntArray(NO_OF_COLORS)
    private val colorPosition = FloatArray(NO_OF_COLORS)
    private val matrix = Matrix()

    private lateinit var shader: Shader
    private var innerCircleArcReference: RectF? = null

    protected abstract val arcLength: Float
    protected abstract val arcStartAngle: Float

    override fun drawComponent(canvas: Canvas) {
        drawShader(canvas)
        drawIndicator(canvas)
    }

    private fun drawShader(canvas: Canvas) {
        val shaderPaint = paints.shaderPaint

        shaderPaint.shader = getShader()
        shaderPaint.style = Paint.Style.STROKE
        shaderPaint.strokeWidth = strokeWidth
        shaderPaint.strokeCap = Paint.Cap.ROUND

        if (innerCircleArcReference == null) {
            innerCircleArcReference = RectF(metrics.centerX - radius, metrics.centerY - radius, metrics.centerX + radius, metrics.centerY + radius)
        }
        canvas.drawArc(innerCircleArcReference, arcStartAngle, arcLength, false, shaderPaint)
    }


    private fun drawIndicator(canvas: Canvas) {
        indicatorX = (metrics.centerX + radius * Math.cos(Math.toRadians(angle))).toFloat()
        indicatorY = (metrics.centerY + radius * Math.sin(Math.toRadians(angle))).toFloat()

        val indicatorPaint = paints.indicatorPaint
        indicatorPaint.style = Paint.Style.FILL

        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(metrics.color, hsl)

        indicatorPaint.color = ColorUtils.HSLToColor(floatArrayOf(hsl[0], getSaturation(angle), 0.5f))
        canvas.drawCircle(indicatorX, indicatorY, indicatorSize, indicatorPaint)

        indicatorPaint.style = Paint.Style.STROKE
        indicatorPaint.strokeWidth = 5f
        indicatorPaint.color = Color.WHITE
        canvas.drawCircle(indicatorX, indicatorY, indicatorSize, indicatorPaint)
    }

    private fun getSaturation(saturationAngle: Double): Float {
        val baseAngle = saturationAngle - arcStartAngle
        Log.d("HSLColorPicker", "sAngle=$saturationAngle, bAngle=$baseAngle, sat=${(baseAngle / arcLength).toFloat()}")
        return (baseAngle / arcLength).toFloat()
    }

    override fun getShader(): Shader {
        with(metrics) {
            getColorArray(color)
            getColorPositionArray()
            shader = SweepGradient(centerX, centerY, colors, colorPosition)
            matrix.setRotate(100f, centerX, centerY) // TODO This 100 should be calculated based on the stroke width
            shader.setLocalMatrix(matrix)
        }

        return shader
    }

    private fun getColorArray(color: Int): IntArray {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(color, hsl)

        for (i in 0 until NO_OF_COLORS) {
            hsl[1] = i.toFloat() / (NO_OF_COLORS - 1)
            colors[i] = ColorUtils.HSLToColor(hsl)
        }
        return colors
    }

    private fun getColorPositionArray(): FloatArray {
        for (i in 0 until NO_OF_COLORS) {
            colorPosition[i] = i * 15f / 360f
        }
        return colorPosition
    }
}