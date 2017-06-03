package com.madrapps.pikolo

import android.graphics.*

class HueComponent(metrics: Metrics, paints: Paints) : ColorComponent(metrics, paints) {

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
        canvas.drawCircle(indicatorX, indicatorY, indicatorSize, indicatorPaint)

        indicatorPaint.style = Paint.Style.STROKE
        indicatorPaint.strokeWidth = 5f
        indicatorPaint.color = Color.WHITE
        canvas.drawCircle(indicatorX, indicatorY, indicatorSize, indicatorPaint)
    }

    override fun calculateAngle(x1: Float, y1: Float) {
        super.calculateAngle(x1, y1)
        onHueChanged(angle)
    }

    private fun onHueChanged(hue: Double) {
        metrics.color = Color.HSVToColor(floatArrayOf(hue.toFloat(), 1f, 1f))
    }
}
