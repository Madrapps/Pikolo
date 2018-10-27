package com.madrapps.pikolo.components

import com.madrapps.pikolo.Metrics
import com.madrapps.pikolo.Paints

internal class LightnessComponent(metrics: Metrics, paints: Paints, override val arcLength: Float, override val arcStartAngle: Float) : ArcComponent(metrics, paints) {
    override val range: Float = 1f
    override val hslIndex: Int = 2
    override val noOfColors = 11
    override val colors = IntArray(noOfColors)
    override val colorPosition = FloatArray(noOfColors)

    init {
        angle = (arcStartAngle + arcLength / 2f).toDouble()
    }

}