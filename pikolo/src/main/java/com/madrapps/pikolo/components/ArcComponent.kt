package com.madrapps.pikolo.components

import android.graphics.*
import android.support.v4.graphics.ColorUtils
import com.madrapps.pikolo.Metrics
import com.madrapps.pikolo.Paints

internal abstract class ArcComponent(metrics: Metrics, paints: Paints) : ColorComponent(metrics, paints) {

    abstract val NO_OF_COLORS: Int
    internal abstract val colors: IntArray
    internal abstract val colorPosition: FloatArray

    private val matrix = Matrix()
    private lateinit var shader: Shader
    private var innerCircleArcReference: RectF? = null
    private val borderColor = floatArrayOf(0f, 0.8f, 1f)

    abstract val hslIndex: Int
    abstract val arcLength: Float
    abstract val arcStartAngle: Float
    /**
     * This is the max value of the component. For now the min value is taken as 0
     */
    abstract val range: Float

    val arcEndAngle: Float
        get() {
            val end = arcStartAngle + arcLength
            if (end > 360f) return end - 360f else return end
        }

    override fun drawComponent(canvas: Canvas) {
        drawArc(canvas)
        drawIndicator(canvas)
    }

    internal open fun drawArc(canvas: Canvas) {
        val shaderPaint = paints.shaderPaint
        shaderPaint.style = Paint.Style.STROKE
        shaderPaint.strokeCap = Paint.Cap.ROUND

        if (innerCircleArcReference == null) {
            innerCircleArcReference = RectF(metrics.centerX - radius, metrics.centerY - radius, metrics.centerX + radius, metrics.centerY + radius)
        }
        if (borderWidth > 0) {
            shaderPaint.shader = null
            shaderPaint.color = if (strokeColor == 0) Color.WHITE else strokeColor
            shaderPaint.strokeWidth = strokeWidth + borderWidth*2
            canvas.drawArc(innerCircleArcReference, arcStartAngle, arcLength, false, shaderPaint)
        }

        shaderPaint.strokeWidth = strokeWidth
        shaderPaint.shader = getShader()
        canvas.drawArc(innerCircleArcReference, arcStartAngle, arcLength, false, shaderPaint)
    }


    internal open fun drawIndicator(canvas: Canvas) {
        indicatorX = (metrics.centerX + radius * Math.cos(Math.toRadians(angle))).toFloat()
        indicatorY = (metrics.centerY + radius * Math.sin(Math.toRadians(angle))).toFloat()

        val indicatorPaint = paints.indicatorPaint
        indicatorPaint.style = Paint.Style.FILL

        val color = ColorUtils.HSLToColor(metrics.hsl)
        indicatorPaint.color = color
        canvas.drawCircle(indicatorX, indicatorY, indicatorRadius, indicatorPaint)

        if (indicatorStrokeWidth > 0) {
            indicatorPaint.color = getBorderColor(color)
            indicatorPaint.style = Paint.Style.STROKE
            indicatorPaint.strokeWidth = indicatorStrokeWidth
            canvas.drawCircle(indicatorX, indicatorY, indicatorRadius, indicatorPaint)
        }
    }

    private fun getBorderColor(color: Int): Int {
        if (indicatorStrokeColor != 0) {
            return indicatorStrokeColor
        }
        borderColor[0] = metrics.hsl[0]
        val contrastW = ColorUtils.calculateContrast(color, Color.WHITE)
        val contrastB = ColorUtils.calculateContrast(color, Color.BLACK)
        when {
            contrastB - contrastW > 16 -> borderColor[2] = 0f
            contrastB - contrastW > 10 -> borderColor[2] = 0.1f
            contrastB - contrastW > 6 -> borderColor[2] = 0.2f
            contrastB - contrastW > 4 -> borderColor[2] = 0.3f
            contrastB - contrastW > 2 -> borderColor[2] = 0.4f
            contrastB - contrastW > 0 -> borderColor[2] = 0.5f
            contrastB - contrastW > -2 -> borderColor[2] = 0.6f
            contrastB - contrastW > -4 -> borderColor[2] = 0.7f
            contrastB - contrastW > -8 -> borderColor[2] = 0.8f
            contrastB - contrastW > -12 -> borderColor[2] = 0.9f
            else -> borderColor[2] = 1f
        }
        return ColorUtils.HSLToColor(borderColor)
    }

    override fun getShader(): Shader {
        with(metrics) {
            getColorArray(hsl.copyOf())
            getColorPositionArray()
            shader = SweepGradient(centerX, centerY, colors, colorPosition)
            // We need a margin of rotation due to the Paint.Cap.Round
            matrix.setRotate(arcStartAngle - (strokeWidth / 3f / metrics.density), centerX, centerY)
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
        // Don't let the indicator go outside the arc
        // limit the indicator between arcStartAngle and arcEndAngle
        val associatedArcLength = 360f - arcLength
        val middleOfAssociatedArc = arcEndAngle + associatedArcLength / 2f
        if (arcEndAngle < arcStartAngle) {
            calculateAngleInContinuousRange(middleOfAssociatedArc)
        } else if (arcEndAngle > arcStartAngle) {
            calculateAngleInNonContinuousRange(middleOfAssociatedArc)
        }
    }

    /**
     * This would be the case when [arcStartAngle]=285 and [arcEndAngle]=75, so that the arc has the 0 degree crossover. This means that the
     * associated arc (360 - [arcLength]) is a continuous range. When the angle is in this range, we need to either set it to the [arcStartAngle]
     * or the [arcEndAngle]
     *
     * @param middle the middle point (in angle) of the associated arc
     */
    private fun calculateAngleInContinuousRange(middle: Float) {
        when (angle) {
            in arcEndAngle..middle -> angle = arcEndAngle.toDouble()
            in middle..arcStartAngle -> angle = arcStartAngle.toDouble()
        }
    }

    /**
     * This is the case where the arc is a continuous range, i.e, the 0 crossover occurs in the associated arc. This can happen in two ways.
     *
     * 1. The [middle] point can be before the 0 degree. Eg. [arcStartAngle]=10 and [arcEndAngle]=120
     * 2. The [middle] point can be after the 0 degree. Eg. [arcStartAngle]=100 and [arcEndAngle]=350
     *
     * @param middle the middle point (in angle) of the associated arc
     */
    private fun calculateAngleInNonContinuousRange(middle: Float) {
        if (middle > 360f) {
            val correctedMiddle = middle - 360f
            when (angle) {
                in arcEndAngle..360f, in 0f..correctedMiddle -> angle = arcEndAngle.toDouble()
                in correctedMiddle..arcStartAngle -> angle = arcStartAngle.toDouble()
            }
        } else {
            when (angle) {
                in arcEndAngle..middle -> angle = arcEndAngle.toDouble()
                in middle..360f, in 0f..arcStartAngle -> angle = arcStartAngle.toDouble()
            }
        }
    }

    override fun updateComponent(angle: Double) {
        var relativeAngle = angle
        if (angle < arcStartAngle) {
            relativeAngle += 360f
        }

        val baseAngle = relativeAngle - arcStartAngle
        val component = (baseAngle / arcLength) * range

        metrics.hsl[hslIndex] = component.toFloat()
    }

    override fun updateAngle(component: Float) {
        val baseAngle = component / range * arcLength
        val relativeAngle = baseAngle + arcStartAngle

        angle = relativeAngle.toDouble()
    }

}