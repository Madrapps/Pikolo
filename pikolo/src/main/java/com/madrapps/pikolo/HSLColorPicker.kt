package com.madrapps.pikolo

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class HSLColorPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val metrics = Metrics()
    private val paints = Paints()

    private val hueComponent: ColorComponent = HueComponent(metrics, paints)
    private val saturationComponent: ColorComponent = SaturationComponent(metrics, paints)

    private val INDICATOR_RADIUS = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)
    private val STROKE_WIDTH = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)

    init {
        hueComponent.strokeWidth = STROKE_WIDTH
        hueComponent.indicatorSize = INDICATOR_RADIUS
        saturationComponent.strokeWidth = STROKE_WIDTH
        saturationComponent.indicatorSize = INDICATOR_RADIUS

        saturationComponent.angle = 240.0
    }

    override fun onDraw(canvas: Canvas) {
        hueComponent.drawComponent(canvas)
        saturationComponent.drawComponent(canvas)
    }

    override fun onSizeChanged(width: Int, height: Int, oldW: Int, oldH: Int) {
        val minimumSize = if (width > height) height else width

        val padding = (paddingLeft + paddingRight + paddingTop + paddingBottom) / 4f

        hueComponent.radius = minimumSize.toFloat() / 2f - padding - STROKE_WIDTH - INDICATOR_RADIUS
        saturationComponent.radius = hueComponent.radius - 80f

        metrics.centerX = width / 2f
        metrics.centerY = height / 2f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!hueComponent.onTouchEvent(event)) {
            saturationComponent.onTouchEvent(event)
        }
        invalidate()
        return true
    }
}