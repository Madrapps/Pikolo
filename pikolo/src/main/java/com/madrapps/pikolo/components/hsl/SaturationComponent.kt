package com.madrapps.pikolo.components.hsl

import androidx.core.graphics.ColorUtils
import com.madrapps.pikolo.Metrics
import com.madrapps.pikolo.Paints
import com.madrapps.pikolo.components.ArcComponent

internal class SaturationComponent(metrics: Metrics, paints: Paints, arcLength: Float, arcStartAngle: Float) : ArcComponent(metrics, paints, arcLength, arcStartAngle) {

    override val componentIndex: Int = 1
    override val range: Float = 1f
    override val noOfColors = 11 // TODO 2 should be sufficient
    override val colors = IntArray(noOfColors)
    override val colorPosition = FloatArray(noOfColors)

    override fun getColorArray(color: FloatArray): IntArray {
        for (i in 0 until noOfColors) {
            color[componentIndex] = i.toFloat() / (noOfColors - 1)
            colors[i] = ColorUtils.HSLToColor(color)
        }
        return colors
    }
}