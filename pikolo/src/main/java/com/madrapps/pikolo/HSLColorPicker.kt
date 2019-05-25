package com.madrapps.pikolo

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.graphics.ColorUtils
import com.madrapps.pikolo.components.ColorComponent
import com.madrapps.pikolo.components.hsl.HslMetrics
import com.madrapps.pikolo.components.hsl.HueComponent
import com.madrapps.pikolo.components.hsl.LightnessComponent
import com.madrapps.pikolo.components.hsl.SaturationComponent
import com.madrapps.pikolo.listeners.OnColorSelectionListener

open class HSLColorPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ColorPicker(context, attrs, defStyleAttr) {

    private val metrics = HslMetrics(color = floatArrayOf(0f, 1f, 0.5f), density = resources.displayMetrics.density)

    private val hueComponent: ColorComponent
    private val saturationComponent: ColorComponent
    private val lightnessComponent: ColorComponent

    private val hueRadiusOffset: Float
    private val saturationRadiusOffset: Float
    private val lightnessRadiusOffset: Float

    override val color: Int
        get() = metrics.getColor()

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HSLColorPicker, defStyleAttr, 0)

        with(config) {
            val hueArcLength = typedArray.getFloat(R.styleable.HSLColorPicker_hue_arc_length, if (arcLength == 0f) 360f else arcLength)
            val hueStartAngle = typedArray.getFloat(R.styleable.HSLColorPicker_hue_start_angle, 0f)
            hueComponent = HueComponent(metrics, paints, hueArcLength, hueStartAngle).also {
                it.fillWidth = typedArray.getDimension(R.styleable.HSLColorPicker_hue_arc_width, arcWidth)
                it.strokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_hue_stroke_width, strokeWidth)
                it.indicatorStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_hue_indicator_stroke_width, indicatorStrokeWidth)
                it.indicatorStrokeColor = typedArray.getColor(R.styleable.HSLColorPicker_hue_indicator_stroke_color, indicatorStrokeColor)
                it.strokeColor = typedArray.getColor(R.styleable.HSLColorPicker_hue_stroke_color, strokeColor)
                it.indicatorRadius = typedArray.getDimension(R.styleable.HSLColorPicker_hue_indicator_radius, indicatorRadius)
            }

            val saturationArcLength = typedArray.getFloat(R.styleable.HSLColorPicker_saturation_arc_length, if (arcLength == 0f) 155f else arcLength)
            val saturationStartAngle = typedArray.getFloat(R.styleable.HSLColorPicker_saturation_start_angle, 100f)
            saturationComponent = SaturationComponent(metrics, paints, saturationArcLength, saturationStartAngle).also {
                it.fillWidth = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_arc_width, arcWidth)
                it.strokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_stroke_width, strokeWidth)
                it.indicatorStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_indicator_stroke_width, indicatorStrokeWidth)
                it.indicatorStrokeColor = typedArray.getColor(R.styleable.HSLColorPicker_saturation_indicator_stroke_color, indicatorStrokeColor)
                it.strokeColor = typedArray.getColor(R.styleable.HSLColorPicker_saturation_stroke_color, strokeColor)
                it.indicatorRadius = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_indicator_radius, indicatorRadius)
            }

            val lightnessArcLength = typedArray.getFloat(R.styleable.HSLColorPicker_lightness_arc_length, if (arcLength == 0f) 155f else arcLength)
            val lightnessStartAngle = typedArray.getFloat(R.styleable.HSLColorPicker_lightness_start_angle, 280f)
            lightnessComponent = LightnessComponent(metrics, paints, lightnessArcLength, lightnessStartAngle).also {
                it.fillWidth = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_arc_width, arcWidth)
                it.strokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_stroke_width, strokeWidth)
                it.indicatorStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_indicator_stroke_width, indicatorStrokeWidth)
                it.indicatorStrokeColor = typedArray.getColor(R.styleable.HSLColorPicker_lightness_indicator_stroke_color, indicatorStrokeColor)
                it.strokeColor = typedArray.getColor(R.styleable.HSLColorPicker_lightness_stroke_color, strokeColor)
                it.indicatorRadius = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_indicator_radius, indicatorRadius)
            }

            hueRadiusOffset = typedArray.getDimension(R.styleable.HSLColorPicker_hue_radius_offset, if (radiusOffset == 0f) dp(1f) else radiusOffset)
            saturationRadiusOffset = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_radius_offset, if (radiusOffset == 0f) dp(25f) else radiusOffset)
            lightnessRadiusOffset = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_radius_offset, if (radiusOffset == 0f) dp(25f) else radiusOffset)
        }
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
        var isTouched = true
        if (!hueComponent.onTouchEvent(event)) {
            if (!saturationComponent.onTouchEvent(event)) {
                isTouched = lightnessComponent.onTouchEvent(event)
            }
        }
        invalidate()
        return isTouched
    }

    override fun setColorSelectionListener(listener: OnColorSelectionListener) {
        hueComponent.setColorSelectionListener(listener)
        saturationComponent.setColorSelectionListener(listener)
        lightnessComponent.setColorSelectionListener(listener)
    }

    override fun setColor(color: Int) {
        with(metrics) {
            ColorUtils.colorToHSL(color, this.color)
            hueComponent.updateAngle(this.color[0])
            saturationComponent.updateAngle(this.color[1])
            lightnessComponent.updateAngle(this.color[2])
        }
        invalidate()
    }
}