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

        val globalArcWidth = typedArray.getDimension(R.styleable.HSLColorPicker_arc_width, dp(5f))
        val globalStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_stroke_width, 0f)
        val globalIndicatorRadius = typedArray.getDimension(R.styleable.HSLColorPicker_indicator_radius, dp(15f))
        val globalIndicatorStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_indicator_stroke_width, dp(2f))
        val globalStrokeColor = typedArray.getColor(R.styleable.HSLColorPicker_stroke_color, 0)
        val globalIndicatorStrokeColor = typedArray.getColor(R.styleable.HSLColorPicker_indicator_stroke_color, 0)
        val globalArcLength = typedArray.getFloat(R.styleable.HSLColorPicker_arc_length, 0f)
        val globalRadiusOffset = typedArray.getDimension(R.styleable.HSLColorPicker_radius_offset, 0f)

        val saturationArcLength = typedArray.getFloat(R.styleable.HSLColorPicker_saturation_arc_length, if (globalArcLength == 0f) 155f else globalArcLength)
        val saturationStartAngle = typedArray.getFloat(R.styleable.HSLColorPicker_saturation_start_angle, 100f)

        saturationComponent = SaturationComponent(metrics, paints, saturationArcLength, saturationStartAngle)

        val lightnessArcLength = typedArray.getFloat(R.styleable.HSLColorPicker_lightness_arc_length, if (globalArcLength == 0f) 155f else globalArcLength)
        val lightnessStartAngle = typedArray.getFloat(R.styleable.HSLColorPicker_lightness_start_angle, 280f)

        lightnessComponent = LightnessComponent(metrics, paints, lightnessArcLength, lightnessStartAngle)

        val hueArcLength = typedArray.getFloat(R.styleable.HSLColorPicker_hue_arc_length, if (globalArcLength == 0f) 360f else globalArcLength)
        val hueStartAngle = typedArray.getFloat(R.styleable.HSLColorPicker_hue_start_angle, 0f)

        hueComponent = HueComponent(metrics, paints, hueArcLength, hueStartAngle)

        hueComponent.fillWidth = typedArray.getDimension(R.styleable.HSLColorPicker_hue_arc_width, globalArcWidth)
        hueComponent.strokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_hue_stroke_width, globalStrokeWidth)
        hueComponent.indicatorStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_hue_indicator_stroke_width, globalIndicatorStrokeWidth)
        hueComponent.indicatorStrokeColor = typedArray.getColor(R.styleable.HSLColorPicker_hue_indicator_stroke_color, globalIndicatorStrokeColor)
        hueComponent.strokeColor = typedArray.getColor(R.styleable.HSLColorPicker_hue_stroke_color, globalStrokeColor)
        hueComponent.indicatorRadius = typedArray.getDimension(R.styleable.HSLColorPicker_hue_indicator_radius, globalIndicatorRadius)

        saturationComponent.fillWidth = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_arc_width, globalArcWidth)
        saturationComponent.strokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_stroke_width, globalStrokeWidth)
        saturationComponent.indicatorStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_indicator_stroke_width, globalIndicatorStrokeWidth)
        saturationComponent.indicatorStrokeColor = typedArray.getColor(R.styleable.HSLColorPicker_saturation_indicator_stroke_color, globalIndicatorStrokeColor)
        saturationComponent.strokeColor = typedArray.getColor(R.styleable.HSLColorPicker_saturation_stroke_color, globalStrokeColor)
        saturationComponent.indicatorRadius = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_indicator_radius, globalIndicatorRadius)

        lightnessComponent.fillWidth = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_arc_width, globalArcWidth)
        lightnessComponent.strokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_stroke_width, globalStrokeWidth)
        lightnessComponent.indicatorStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_indicator_stroke_width, globalIndicatorStrokeWidth)
        lightnessComponent.indicatorStrokeColor = typedArray.getColor(R.styleable.HSLColorPicker_lightness_indicator_stroke_color, globalIndicatorStrokeColor)
        lightnessComponent.strokeColor = typedArray.getColor(R.styleable.HSLColorPicker_lightness_stroke_color, globalStrokeColor)
        lightnessComponent.indicatorRadius = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_indicator_radius, globalIndicatorRadius)

        hueRadiusOffset = typedArray.getDimension(R.styleable.HSLColorPicker_hue_radius_offset, if (globalRadiusOffset == 0f) dp(1f) else globalRadiusOffset)
        saturationRadiusOffset = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_radius_offset, if (globalRadiusOffset == 0f) dp(25f) else globalRadiusOffset)
        lightnessRadiusOffset = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_radius_offset, if (globalRadiusOffset == 0f) dp(25f) else globalRadiusOffset)

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