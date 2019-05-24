package com.madrapps.pikolo.components.hsl

import androidx.core.graphics.ColorUtils
import com.madrapps.pikolo.Metrics
import com.madrapps.pikolo.Paints
import com.madrapps.pikolo.components.ArcComponent

internal class LightnessComponent(metrics: Metrics, paints: Paints, arcLength: Float, arcStartAngle: Float) : ArcComponent(metrics, paints, arcLength, arcStartAngle) {

    private val hslIndex: Int = 2
    override val range: Float = 1f
    override val noOfColors = 11 // TODO 3 should be sufficient
    override val colors = IntArray(noOfColors)
    override val colorPosition = FloatArray(noOfColors)

    override fun updateComponent(angle: Double): Float {
        val component =  super.updateComponent(angle)
        metrics.color[hslIndex] = component
        return component
    }

    override fun getColorArray(color: FloatArray): IntArray {
        for (i in 0 until noOfColors) {
            color[hslIndex] = i.toFloat() / (noOfColors - 1)
            colors[i] = ColorUtils.HSLToColor(color)
        }
        return colors
    }
}