package com.madrapps.pikolo

import android.graphics.Paint
import android.support.v4.graphics.ColorUtils

data class Metrics(var centerX: Float = 0f, var centerY: Float = 0f, var hsl: FloatArray = floatArrayOf(0f, 1f, 0.5f))

data class Paints(val shaderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG), val indicatorPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG))

fun Metrics.color() = ColorUtils.HSLToColor(hsl)