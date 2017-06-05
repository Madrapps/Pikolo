package com.madrapps.pikolo.components

import com.madrapps.pikolo.Metrics
import com.madrapps.pikolo.Paints

internal class LightnessComponent(metrics: Metrics, paints: Paints, override val arcLength: Float, override val arcStartAngle: Float) : ArcComponent(metrics, paints) {

    override val hslIndex: Int = 2

    init {
        angle = (arcStartAngle + arcLength / 2f).toDouble()
    }

}