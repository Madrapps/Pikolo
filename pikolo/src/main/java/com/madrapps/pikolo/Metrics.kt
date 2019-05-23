package com.madrapps.pikolo

import android.graphics.Paint
import androidx.core.graphics.ColorUtils
import java.util.*

data class Metrics(var centerX: Float = 0f, var centerY: Float = 0f, var hsl: FloatArray = floatArrayOf(0f, 1f, 0.5f), val density: Float) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Metrics

        if (centerX != other.centerX) return false
        if (centerY != other.centerY) return false
        if (!Arrays.equals(hsl, other.hsl)) return false
        if (density != other.density) return false

        return true
    }

    override fun hashCode(): Int {
        var result = centerX.hashCode()
        result = 31 * result + centerY.hashCode()
        result = 31 * result + Arrays.hashCode(hsl)
        result = 31 * result + density.hashCode()
        return result
    }
}

data class Paints(val shaderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG), val indicatorPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG))

fun Metrics.color() = ColorUtils.HSLToColor(hsl)