package com.madrapps.pikolo.components

import com.madrapps.pikolo.Metrics
import com.madrapps.pikolo.Paints

internal class LightnessComponent(metrics: Metrics, paints: Paints, override val arcLength: Float, override val arcStartAngle: Float) : ArcComponent(metrics, paints) {
    override val range: Float = 1f
    override val hslIndex: Int = 2
    override val NO_OF_COLORS = 11
    override val colors = IntArray(NO_OF_COLORS)
    override val colorPosition = FloatArray(NO_OF_COLORS)

    init {
        angle = (arcStartAngle + arcLength / 2f).toDouble()
    }

}