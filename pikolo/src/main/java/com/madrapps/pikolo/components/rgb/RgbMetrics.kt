package com.madrapps.pikolo.components.rgb

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import com.madrapps.pikolo.Metrics

internal class RgbMetrics(centerX: Float = 0f, centerY: Float = 0f, color: FloatArray, density: Float) : Metrics(centerX, centerY, color, density) {

    override fun hue(): Float {
        val hsl = FloatArray(3)
        ColorUtils.RGBToHSL(color[0].toInt(), color[1].toInt(), color[2].toInt(), hsl)
        return hsl[0]
    }

    override fun getColor() = Color.rgb(color[0].toInt(), color[1].toInt(), color[2].toInt())
}