package com.madrapps.pikolo.components

import androidx.core.graphics.ColorUtils
import com.madrapps.pikolo.Metrics
import com.madrapps.pikolo.Paints

internal class HueComponent(metrics: Metrics, paints: Paints, override val arcLength: Float, override val arcStartAngle: Float) : ArcComponent(metrics, paints) {
    override val range: Float = 360f
    override val hslIndex: Int = 0
    override val noOfColors = 360
    override val colors = IntArray(noOfColors)
    override val colorPosition = FloatArray(noOfColors)

    init {
        angle = (arcStartAngle + arcLength / 2f).toDouble()
    }

    override fun getColorArray(hsl: FloatArray): IntArray {
        hsl[1] = 1f
        hsl[2] = 0.5f
        for (i in 0 until noOfColors) {
            hsl[hslIndex] = i.toFloat()
            colors[i] = ColorUtils.HSLToColor(hsl)
        }
        return colors
    }

}
