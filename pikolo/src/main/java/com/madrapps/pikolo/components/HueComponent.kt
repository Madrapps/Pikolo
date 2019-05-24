package com.madrapps.pikolo.components

import android.support.v4.graphics.ColorUtils
import com.madrapps.pikolo.Metrics
import com.madrapps.pikolo.Paints

internal class HueComponent(metrics: Metrics, paints: Paints, arcLength: Float, arcStartAngle: Float) : ArcComponent(metrics, paints, arcLength, arcStartAngle) {

    override val range: Float = 360f
    override val hslIndex: Int = 0
    override val noOfColors = 360
    override val colors = IntArray(noOfColors)
    override val colorPosition = FloatArray(noOfColors)

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
