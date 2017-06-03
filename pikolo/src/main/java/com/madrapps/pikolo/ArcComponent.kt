package com.madrapps.pikolo

import android.graphics.*
import android.support.v4.graphics.ColorUtils

abstract class ArcComponent(metrics: Metrics, paints: Paints) : ColorComponent(metrics, paints) {
    internal val NO_OF_COLORS = 11

    internal val colors = IntArray(NO_OF_COLORS)
    private val colorPosition = FloatArray(NO_OF_COLORS)
    private val matrix = Matrix()

    private lateinit var shader: Shader
    private var innerCircleArcReference: RectF? = null

    protected abstract val arcLength: Float
    protected abstract val arcStartAngle: Float
    internal abstract val hslIndex: Int

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

        indicatorPaint.color = ColorUtils.HSLToColor(metrics.hsl)
        canvas.drawCircle(indicatorX, indicatorY, indicatorRadius, indicatorPaint)

        indicatorPaint.style = Paint.Style.STROKE
        indicatorPaint.strokeWidth = indicatorStrokeWidth
        indicatorPaint.color = Color.WHITE
        canvas.drawCircle(indicatorX, indicatorY, indicatorRadius, indicatorPaint)
    }

    override fun getShader(): Shader {
        with(metrics) {
            getColorArray(hsl.copyOf())
            getColorPositionArray()
            shader = SweepGradient(centerX, centerY, colors, colorPosition)
            // We need a margin of rotation due to the Paint.Cap.Round
            matrix.setRotate(arcStartAngle - strokeWidth/2f, centerX, centerY)
            shader.setLocalMatrix(matrix)
        }

        return shader
    }

    internal open fun getColorArray(hsl: FloatArray): IntArray {
        for (i in 0..NO_OF_COLORS - 1) {
            hsl[hslIndex] = i.toFloat() / (NO_OF_COLORS - 1)
            colors[i] = ColorUtils.HSLToColor(hsl)
        }
        return colors
    }

    private fun getColorPositionArray(): FloatArray {
        for (i in 0..NO_OF_COLORS - 1) {
            colorPosition[i] = i * (arcLength / (NO_OF_COLORS - 1)) / 360f
        }
        return colorPosition
    }

    override fun calculateAngle(x1: Float, y1: Float) {
        super.calculateAngle(x1, y1)

        // limit the angle between (arcStartAngle) and (arcStartAngle + arcLength)
        var arcEndAngle = arcStartAngle + arcLength

        if (arcEndAngle > 360f) {
            // The shader range is not contiguous. But the non-shader range is contiguous
            arcEndAngle -= 360f
            val outsideArcAngle = arcStartAngle - arcEndAngle
            when (angle) {
                in arcEndAngle..arcEndAngle + outsideArcAngle / 2f -> angle = arcEndAngle.toDouble()
                in arcEndAngle + outsideArcAngle / 2f..arcStartAngle -> angle = arcStartAngle.toDouble()
            }
        } else {
            // The shader range is contiguous. But the non-shader range is non-contiguous
            if (angle < arcStartAngle) {
                angle = arcStartAngle.toDouble()
            } else if (angle > arcEndAngle) {
                angle = arcEndAngle.toDouble()
            }
        }
    }

    override fun updateComponent(angle: Double) {
        var arcEndAngle = arcStartAngle + arcLength
        var relativeAngle = angle

        if (arcEndAngle > 360) {
            arcEndAngle -= 360
            if (relativeAngle in 0f..arcEndAngle) {
                relativeAngle += 360f
            }
        }

        val baseAngle = relativeAngle - arcStartAngle
        val saturation = (baseAngle / arcLength).toFloat()

        metrics.hsl[hslIndex] = saturation
    }
}