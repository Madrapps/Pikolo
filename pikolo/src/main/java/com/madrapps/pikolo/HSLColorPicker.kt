package com.madrapps.pikolo

import android.content.Context
import android.graphics.Canvas
import android.os.Parcel
import android.os.Parcelable
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

    private val metrics = Metrics(density = resources.displayMetrics.density)
    private val paints = Paints()

    private val hueComponent: ColorComponent
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

        val hueArcLength = typedArray.getFloat(R.styleable.HSLColorPicker_hue_arc_length, 360f)
        val hueStartAngle = typedArray.getFloat(R.styleable.HSLColorPicker_hue_start_angle, 0f)

        hueComponent = HueComponent(metrics, paints, hueArcLength, hueStartAngle)

        hueComponent.strokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_hue_arc_width, dp(5f))
        hueComponent.borderWidth = typedArray.getDimension(R.styleable.HSLColorPicker_hue_stroke_width, 0f)
        hueComponent.indicatorStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_hue_indicator_stroke_width, dp(2f))
        hueComponent.indicatorStrokeColor = typedArray.getColor(R.styleable.HSLColorPicker_hue_indicator_stroke_color, 0)
        hueComponent.strokeColor = typedArray.getColor(R.styleable.HSLColorPicker_hue_stroke_color, 0)
        hueComponent.indicatorRadius = typedArray.getDimension(R.styleable.HSLColorPicker_hue_indicator_radius, dp(15f))

        saturationComponent.strokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_arc_width, dp(5f))
        saturationComponent.borderWidth = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_stroke_width, 0f)
        saturationComponent.indicatorStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_indicator_stroke_width, dp(2f))
        saturationComponent.indicatorStrokeColor = typedArray.getColor(R.styleable.HSLColorPicker_saturation_indicator_stroke_color, 0)
        saturationComponent.strokeColor = typedArray.getColor(R.styleable.HSLColorPicker_saturation_stroke_color, 0)
        saturationComponent.indicatorRadius = typedArray.getDimension(R.styleable.HSLColorPicker_saturation_indicator_radius, dp(15f))

        lightnessComponent.strokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_arc_width, dp(5f))
        lightnessComponent.borderWidth = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_stroke_width, 0f)
        lightnessComponent.indicatorStrokeWidth = typedArray.getDimension(R.styleable.HSLColorPicker_lightness_indicator_stroke_width, dp(2f))
        lightnessComponent.indicatorStrokeColor = typedArray.getColor(R.styleable.HSLColorPicker_lightness_indicator_stroke_color, 0)
        lightnessComponent.strokeColor = typedArray.getColor(R.styleable.HSLColorPicker_lightness_stroke_color, 0)
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
        var isTouched = true
        if (!hueComponent.onTouchEvent(event)) {
            if (!saturationComponent.onTouchEvent(event)) {
                isTouched = lightnessComponent.onTouchEvent(event)
            }
        }
        invalidate()
        return isTouched
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


    override fun onSaveInstanceState(): Parcelable {
        val bundle = super.onSaveInstanceState()
        val savedState = SavedState(bundle)
        savedState.color = ColorUtils.HSLToColor(metrics.hsl)
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            setColor(state.color)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    internal class SavedState : BaseSavedState {
        var color: Int = 0

        constructor(bundle: Parcelable) : super(bundle)

        private constructor(parcel: Parcel) : super(parcel) {
            color = parcel.readInt()
        }

        companion object {
            @JvmField val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState = SavedState(source)
                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(color)
        }
    }
}