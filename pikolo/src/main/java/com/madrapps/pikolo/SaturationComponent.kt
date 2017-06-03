package com.madrapps.pikolo

class SaturationComponent(metrics: Metrics, paints: Paints) : ArcComponent(metrics, paints) {

    override val arcLength: Float = 150f
    override val arcStartAngle: Float = 105f

    override fun calculateAngle(x1: Float, y1: Float) {
        super.calculateAngle(x1, y1)

        // limit the angle between (105) and (105 + 150)
        if (angle < arcStartAngle) {
            angle = arcStartAngle.toDouble()
        } else if (angle > arcStartAngle + arcLength) {
            angle = (arcStartAngle + arcLength).toDouble()
        }
    }
}