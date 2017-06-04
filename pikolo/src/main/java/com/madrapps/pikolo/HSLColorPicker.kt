package com.madrapps.pikolo

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.madrapps.pikolo.components.ColorComponent
import com.madrapps.pikolo.components.HueComponent
import com.madrapps.pikolo.components.LightnessComponent
import com.madrapps.pikolo.components.SaturationComponent
import com.madrapps.pikolo.listeners.OnColorSelectionListener

class HSLColorPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val metrics = Metrics()
    private val paints = Paints()

    private val hueComponent: ColorComponent = HueComponent(metrics, paints)
    private val saturationComponent: ColorComponent = SaturationComponent(metrics, paints)
    private val lightnessComponent: ColorComponent = LightnessComponent(metrics, paints)

    private val INDICATOR_RADIUS = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)
    private val STROKE_WIDTH = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
    private val INDICATOR_STROKE_WIDTH = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics)

    private val INNER_RADIUS_GAP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25f, resources.displayMetrics)

    init {
        hueComponent.strokeWidth = STROKE_WIDTH
        hueComponent.indicatorRadius = INDICATOR_RADIUS
        hueComponent.indicatorStrokeWidth = INDICATOR_STROKE_WIDTH

        saturationComponent.strokeWidth = STROKE_WIDTH
        saturationComponent.indicatorRadius = INDICATOR_RADIUS
        saturationComponent.indicatorStrokeWidth = INDICATOR_STROKE_WIDTH

        lightnessComponent.strokeWidth = STROKE_WIDTH
        lightnessComponent.indicatorRadius = INDICATOR_RADIUS
        lightnessComponent.indicatorStrokeWidth = INDICATOR_STROKE_WIDTH

        saturationComponent.angle = 240.0
        lightnessComponent.angle = 60.0
    }

    override fun onDraw(canvas: Canvas) {
        hueComponent.drawComponent(canvas)
        saturationComponent.drawComponent(canvas)
        lightnessComponent.drawComponent(canvas)
    }

    override fun onSizeChanged(width: Int, height: Int, oldW: Int, oldH: Int) {
        val minimumSize = if (width > height) height else width

        val padding = (paddingLeft + paddingRight + paddingTop + paddingBottom) / 4f

        hueComponent.radius = minimumSize.toFloat() / 2f - padding - STROKE_WIDTH - INDICATOR_RADIUS
        saturationComponent.radius = hueComponent.radius - INNER_RADIUS_GAP
        lightnessComponent.radius = saturationComponent.radius

        metrics.centerX = width / 2f
        metrics.centerY = height / 2f

        hueComponent.updateComponent(hueComponent.angle)
        saturationComponent.updateComponent(saturationComponent.angle)
        lightnessComponent.updateComponent(lightnessComponent.angle)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!hueComponent.onTouchEvent(event)) {
            if (!saturationComponent.onTouchEvent(event)) {
                lightnessComponent.onTouchEvent(event)
            }
        }
        invalidate()
        return true
    }

    fun setColorSelectionListener(listener: OnColorSelectionListener) {
        hueComponent.setColorSelectionListener(listener)
        saturationComponent.setColorSelectionListener(listener)
        lightnessComponent.setColorSelectionListener(listener)
    }
}