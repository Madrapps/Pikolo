package com.madrapps.pikolo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import com.madrapps.pikolo.components.ColorComponent
import com.madrapps.pikolo.components.rgb.BlueComponent
import com.madrapps.pikolo.components.rgb.GreenComponent
import com.madrapps.pikolo.components.rgb.RedComponent
import com.madrapps.pikolo.components.rgb.RgbMetrics
import com.madrapps.pikolo.listeners.OnColorSelectionListener

open class RGBColorPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ColorPicker(context, attrs, defStyleAttr) {

    private val metrics = RgbMetrics(color = floatArrayOf(255f, 0f, 0f), density = resources.displayMetrics.density)

    private val redComponent: ColorComponent
    private val greenComponent: ColorComponent
    private val blueComponent: ColorComponent

    private val redRadiusOffset: Float
    private val greenRadiusOffset: Float
    private val blueRadiusOffset: Float

    override val color: Int
        get() = metrics.getColor()

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RGBColorPicker, defStyleAttr, 0)

        with(config) {
            val redArcLength = typedArray.getFloat(R.styleable.RGBColorPicker_red_arc_length, if (arcLength == 0f) 110f else arcLength)
            val redStartAngle = typedArray.getFloat(R.styleable.RGBColorPicker_red_start_angle, 30f)
            redComponent = RedComponent(metrics, paints, redArcLength, redStartAngle).also {
                it.fillWidth = typedArray.getDimension(R.styleable.RGBColorPicker_red_arc_width, arcWidth)
                it.strokeWidth = typedArray.getDimension(R.styleable.RGBColorPicker_red_stroke_width, strokeWidth)
                it.indicatorStrokeWidth = typedArray.getDimension(R.styleable.RGBColorPicker_red_indicator_stroke_width, indicatorStrokeWidth)
                it.indicatorStrokeColor = typedArray.getColor(R.styleable.RGBColorPicker_red_indicator_stroke_color, indicatorStrokeColor)
                it.strokeColor = typedArray.getColor(R.styleable.RGBColorPicker_red_stroke_color, strokeColor)
                it.indicatorRadius = typedArray.getDimension(R.styleable.RGBColorPicker_red_indicator_radius, indicatorRadius)
            }

            val greenArcLength = typedArray.getFloat(R.styleable.RGBColorPicker_green_arc_length, if (arcLength == 0f) 110f else arcLength)
            val greenStartAngle = typedArray.getFloat(R.styleable.RGBColorPicker_green_start_angle, 150f)
            greenComponent = GreenComponent(metrics, paints, greenArcLength, greenStartAngle).also {
                it.fillWidth = typedArray.getDimension(R.styleable.RGBColorPicker_green_arc_width, arcWidth)
                it.strokeWidth = typedArray.getDimension(R.styleable.RGBColorPicker_green_stroke_width, strokeWidth)
                it.indicatorStrokeWidth = typedArray.getDimension(R.styleable.RGBColorPicker_green_indicator_stroke_width, indicatorStrokeWidth)
                it.indicatorStrokeColor = typedArray.getColor(R.styleable.RGBColorPicker_green_indicator_stroke_color, indicatorStrokeColor)
                it.strokeColor = typedArray.getColor(R.styleable.RGBColorPicker_green_stroke_color, strokeColor)
                it.indicatorRadius = typedArray.getDimension(R.styleable.RGBColorPicker_green_indicator_radius, indicatorRadius)
            }

            val blueArcLength = typedArray.getFloat(R.styleable.RGBColorPicker_blue_arc_length, if (arcLength == 0f) 110f else arcLength)
            val blueStartAngle = typedArray.getFloat(R.styleable.RGBColorPicker_blue_start_angle, 270f)

            blueComponent = BlueComponent(metrics, paints, blueArcLength, blueStartAngle).also {
                it.fillWidth = typedArray.getDimension(R.styleable.RGBColorPicker_blue_arc_width, arcWidth)
                it.strokeWidth = typedArray.getDimension(R.styleable.RGBColorPicker_blue_stroke_width, config.strokeWidth)
                it.indicatorStrokeWidth = typedArray.getDimension(R.styleable.RGBColorPicker_blue_indicator_stroke_width, indicatorStrokeWidth)
                it.indicatorStrokeColor = typedArray.getColor(R.styleable.RGBColorPicker_blue_indicator_stroke_color, indicatorStrokeColor)
                it.strokeColor = typedArray.getColor(R.styleable.RGBColorPicker_blue_stroke_color, strokeColor)
                it.indicatorRadius = typedArray.getDimension(R.styleable.RGBColorPicker_blue_indicator_radius, indicatorRadius)
            }

            redRadiusOffset = typedArray.getDimension(R.styleable.RGBColorPicker_red_radius_offset, if (radiusOffset == 0f) dp(25f) else radiusOffset)
            greenRadiusOffset = typedArray.getDimension(R.styleable.RGBColorPicker_green_radius_offset, if (radiusOffset == 0f) dp(25f) else radiusOffset)
            blueRadiusOffset = typedArray.getDimension(R.styleable.RGBColorPicker_blue_radius_offset, if (radiusOffset == 0f) dp(25f) else radiusOffset)
        }
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        redComponent.drawComponent(canvas)
        greenComponent.drawComponent(canvas)
        blueComponent.drawComponent(canvas)
    }

    override fun onSizeChanged(width: Int, height: Int, oldW: Int, oldH: Int) {
        val minimumSize = if (width > height) height else width
        val padding = (paddingLeft + paddingRight + paddingTop + paddingBottom) / 4f
        val outerRadius = minimumSize.toFloat() / 2f - padding

        redComponent.setRadius(outerRadius, redRadiusOffset)
        greenComponent.setRadius(outerRadius, greenRadiusOffset)
        blueComponent.setRadius(outerRadius, blueRadiusOffset)

        metrics.centerX = width / 2f
        metrics.centerY = height / 2f

        redComponent.updateComponent(redComponent.angle)
        greenComponent.updateComponent(greenComponent.angle)
        blueComponent.updateComponent(blueComponent.angle)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var isTouched = true
        if (!redComponent.onTouchEvent(event)) {
            if (!greenComponent.onTouchEvent(event)) {
                isTouched = blueComponent.onTouchEvent(event)
            }
        }
        invalidate()
        return isTouched
    }

    override fun setColorSelectionListener(listener: OnColorSelectionListener) {
        redComponent.setColorSelectionListener(listener)
        greenComponent.setColorSelectionListener(listener)
        blueComponent.setColorSelectionListener(listener)
    }

    override fun setColor(color: Int) {
        val red = Color.red(color).toFloat()
        metrics.color[0] = red
        redComponent.updateAngle(red)

        val green = Color.green(color).toFloat()
        metrics.color[1] = green
        greenComponent.updateAngle(green)

        val blue = Color.blue(color).toFloat()
        metrics.color[2] = blue
        blueComponent.updateAngle(blue)
        invalidate()
    }
}