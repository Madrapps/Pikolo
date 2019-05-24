package com.madrapps.pikolo.components

import android.support.v4.graphics.ColorUtils
import com.madrapps.pikolo.Metrics
import com.madrapps.pikolo.Paints

internal class SaturationComponent(metrics: Metrics, paints: Paints, arcLength: Float, arcStartAngle: Float) : ArcComponent(metrics, paints, arcLength, arcStartAngle) {

    val hslIndex: Int = 1
    override val range: Float = 1f
    override val noOfColors = 11 // TODO 2 should be sufficient
    override val colors = IntArray(noOfColors)
    override val colorPosition = FloatArray(noOfColors)

    override fun updateComponent(angle: Double): Float {
        val component =  super.updateComponent(angle)
        metrics.hsl[hslIndex] = component
        return component
    }

    override fun getColorArray(hsl: FloatArray): IntArray {
        for (i in 0 until noOfColors) {
            hsl[hslIndex] = i.toFloat() / (noOfColors - 1)
            colors[i] = ColorUtils.HSLToColor(hsl)
        }
        return colors
    }
}