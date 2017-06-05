package com.madrapps.pikolo

import android.content.Context
import android.graphics.Canvas
import android.support.v4.graphics.ColorUtils
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
    private val saturationComponent: ColorComponent
    private val lightnessComponent: ColorComponent

    private val hueRadiusOffset: Float
    private val saturationRadiusOffset: Float
    private val lightnessRadiusOffset: Float

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HSLColorPicker, defStyleAttr, 0)

        val saturationArcLength = typedArray.getFloat(R.styleable.HSLColorPicker_saturation_arc_length, 155f)
        val saturationStartAngle = typedArray.getFloat(R.styleable.HSLColorPicker_saturation_start_angle, 100f)

        saturationComponent = SaturationComponent(metrics, paints, saturationArcLength, saturationStartAngle)

        val lightnessArcLength = typedArray.getFloat(R.styleable.HSLColorPicker_lightness_arc_length, 155f)
        val lightnessStartAngle = typedArray.getFloat(R.styleable.HSLColorPicker_lightness_start_angle, 280f)

        lightnessComponent = LightnessComponent(metrics, paints, lightnessArcLength, lightnessStartAngle)

        hueComponent.strokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_hue_stroke_width, dp(5f))
        hueComponent.indicatorStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_hue_indicator_stroke_width, dp(2f))
        hueComponent.indicatorRadius = typedArray.getDimension(R.styleable.HSLColorPicker_hue_indicator_radius, dp(15f))

        saturationComponent.strokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_stroke_width, dp(5f))
        saturationComponent.indicatorStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_indicator_stroke_width, dp(2f))
        saturationComponent.indicatorRadius = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_indicator_radius, dp(15f))

        lightnessComponent.strokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_stroke_width, dp(5f))
        lightnessComponent.indicatorStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_indicator_stroke_width, dp(2f))
        lightnessComponent.indicatorRadius = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_indicator_radius, dp(15f))

        hueRadiusOffset = typedArray.getDimension(R.styleable.HSLColorPicker_hue_radius_offset, dp(1f))
        saturationRadiusOffset = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_radius_offset, dp(25f))
        lightnessRadiusOffset = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_radius_offset, dp(25f))

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        hueComponent.drawComponent(canvas)
        saturationComponent.drawComponent(canvas)
        lightnessComponent.drawComponent(canvas)
    }

    override fun onSizeChanged(width: Int, height: Int, oldW: Int, oldH: Int) {
        val minimumSize = if (width > height) height else width
        val padding = (paddingLeft + paddingRight + paddingTop + paddingBottom) / 4f
        val outerRadius = minimumSize.toFloat() / 2f - padding

        hueComponent.setRadius(outerRadius, hueRadiusOffset)
        saturationComponent.setRadius(outerRadius, saturationRadiusOffset)
        lightnessComponent.setRadius(outerRadius, lightnessRadiusOffset)

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

    fun setColor(color: Int) {
        with(metrics) {
            ColorUtils.colorToHSL(color, hsl)
            hueComponent.updateAngle(hsl[0])
            saturationComponent.updateAngle(hsl[1])
            lightnessComponent.updateAngle(hsl[2])
        }
        invalidate()
    }

    private fun dp(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)
    }

}