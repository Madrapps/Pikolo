package com.madrapps.pikolo.components

import com.madrapps.pikolo.Metrics
import com.madrapps.pikolo.Paints

internal class LightnessComponent(metrics: Metrics, paints: Paints) : ArcComponent(metrics, paints) {

    override val arcLength: Float = 155f
    override val arcStartAngle: Float = 280f
    override val hslIndex: Int = 2

}