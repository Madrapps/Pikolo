package com.madrapps.pikolo.components.hsl

import androidx.core.graphics.ColorUtils
import com.madrapps.pikolo.Metrics

internal class HslMetrics(centerX: Float = 0f, centerY: Float = 0f, color: FloatArray, density: Float) : Metrics(centerX, centerY, color, density) {

    override fun hue() = color[0]

    override fun getColor() = ColorUtils.HSLToColor(color)
}