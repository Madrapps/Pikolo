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
        val globalArcWidth = dp(5f)
        val globalStrokeWidth = 0f
        val globalIndicatorRadius = dp(15f)
        val globalIndicatorStrokeWidth = dp(2f)
        val globalStrokeColor = 0
        val globalIndicatorStrokeColor = 0
        val globalArcLength = 110f
        val globalRadiusOffset = 20f

        val redArcLength = if (globalArcLength == 0f) 360f else globalArcLength
        val redStartAngle = 30f

        val greenArcLength = if (globalArcLength == 0f) 360f else globalArcLength
        val greenStartAngle = 150f

        val blueArcLength = if (globalArcLength == 0f) 360f else globalArcLength
        val blueStartAngle = 270f

        redComponent = RedComponent(metrics, paints, redArcLength, redStartAngle)
        greenComponent = GreenComponent(metrics, paints, greenArcLength, greenStartAngle)
        blueComponent = BlueComponent(metrics, paints, blueArcLength, blueStartAngle)

        redComponent.fillWidth = globalArcWidth
        redComponent.strokeWidth = globalStrokeWidth
        redComponent.indicatorStrokeWidth = globalIndicatorStrokeWidth
        redComponent.indicatorStrokeColor = globalIndicatorStrokeColor
        redComponent.strokeColor = globalStrokeColor
        redComponent.indicatorRadius = globalIndicatorRadius

        redRadiusOffset = if (globalRadiusOffset == 0f) dp(1f) else globalRadiusOffset

        greenComponent.fillWidth = globalArcWidth
        greenComponent.strokeWidth = globalStrokeWidth
        greenComponent.indicatorStrokeWidth = globalIndicatorStrokeWidth
        greenComponent.indicatorStrokeColor = globalIndicatorStrokeColor
        greenComponent.strokeColor = globalStrokeColor
        greenComponent.indicatorRadius = globalIndicatorRadius

        greenRadiusOffset = if (globalRadiusOffset == 0f) dp(1f) else globalRadiusOffset

        blueComponent.fillWidth = globalArcWidth
        blueComponent.strokeWidth = globalStrokeWidth
        blueComponent.indicatorStrokeWidth = globalIndicatorStrokeWidth
        blueComponent.indicatorStrokeColor = globalIndicatorStrokeColor
        blueComponent.strokeColor = globalStrokeColor
        blueComponent.indicatorRadius = globalIndicatorRadius

        blueRadiusOffset = if (globalRadiusOffset == 0f) dp(1f) else globalRadiusOffset

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