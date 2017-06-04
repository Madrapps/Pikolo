package com.madrapps.pikolo.components

import android.graphics.*
import com.madrapps.pikolo.Metrics
import com.madrapps.pikolo.Paints

internal class HueComponent(metrics: Metrics, paints: Paints) : ColorComponent(metrics, paints) {

    private val colors = IntArray(360)
    private lateinit var shader: Shader

    override fun getShader(): Shader {
        shader = SweepGradient(metrics.centerX, metrics.centerY, getColorArray(), null)
        return shader
    }

    private fun getColorArray(): IntArray {
        val hsv = floatArrayOf(0f, 1f, 1f)
        for (i in 0..359) {
            hsv[0] = i.toFloat()
            colors[i] = Color.HSVToColor(hsv)
        }
        return colors
    }

    override fun drawComponent(canvas: Canvas) {
        drawShader(canvas)
        drawIndicator(canvas)
    }

    private fun drawShader(canvas: Canvas) {
        val shaderPaint = paints.shaderPaint

        shaderPaint.shader = getShader()

        shaderPaint.style = Paint.Style.STROKE
        shaderPaint.strokeWidth = strokeWidth

        canvas.drawCircle(metrics.centerX, metrics.centerY, radius, shaderPaint)
    }

    private fun drawIndicator(canvas: Canvas) {
        indicatorX = (metrics.centerX + radius * Math.cos(Math.toRadians(angle))).toFloat()
        indicatorY = (metrics.centerY + radius * Math.sin(Math.toRadians(angle))).toFloat()

        val indicatorPaint = paints.indicatorPaint
        indicatorPaint.style = Paint.Style.FILL
        indicatorPaint.color = Color.HSVToColor(floatArrayOf(angle.toFloat(), 1f, 1f))
        canvas.drawCircle(indicatorX, indicatorY, indicatorRadius, indicatorPaint)

        indicatorPaint.style = Paint.Style.STROKE
        indicatorPaint.strokeWidth = indicatorStrokeWidth
        indicatorPaint.color = Color.WHITE
        canvas.drawCircle(indicatorX, indicatorY, indicatorRadius, indicatorPaint)
    }

    override fun updateComponent(angle: Double) {
        metrics.hsl[0] = angle.toFloat()
    }

    override fun updateAngle(component: Float) {
        angle = metrics.hsl[0].toDouble()
    }
}
