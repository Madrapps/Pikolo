package com.madrapps.pikolo

import android.graphics.Color
import android.graphics.Paint

data class Metrics(var centerX: Float = 0f, var centerY: Float = 0f, var color: Int = Color.RED)

data class Paints(val shaderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG), val indicatorPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG))