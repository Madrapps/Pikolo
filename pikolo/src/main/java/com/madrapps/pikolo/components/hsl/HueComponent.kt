package com.madrapps.pikolo.components.hsl

import androidx.core.graphics.ColorUtils
import com.madrapps.pikolo.Metrics
import com.madrapps.pikolo.Paints
import com.madrapps.pikolo.components.ArcComponent

internal class HueComponent(metrics: Metrics, paints: Paints, arcLength: Float, arcStartAngle: Float) : ArcComponent(metrics, paints, arcLength, arcStartAngle) {

    override val componentIndex = 0
    override val range: Float = 360f
    override val noOfColors = 360
    override val colors = IntArray(noOfColors)
    override val colorPosition = FloatArray(noOfColors)

    override fun getColorArray(color: FloatArray): IntArray {
        color[1] = 1f
        color[2] = 0.5f
        for (i in 0 until noOfColors) {
            color[componentIndex] = i.toFloat()
            colors[i] = ColorUtils.HSLToColor(color)
        }
        return colors
    }
}
