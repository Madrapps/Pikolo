package com.madrapps.pikolo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.ColorUtils
import com.madrapps.pikolo.components.ColorComponent
import com.madrapps.pikolo.components.rgb.RedComponent
import com.madrapps.pikolo.components.rgb.RgbMetrics
import com.madrapps.pikolo.listeners.OnColorSelectionListener


open class RGBColorPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val metrics = RgbMetrics(color = floatArrayOf(255f, 0f, 0f), density = resources.displayMetrics.density)
    private val paints = Paints()

    private val redComponent: ColorComponent

    private val redRadiusOffset: Float

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

        redComponent = RedComponent(metrics, paints, redArcLength, redStartAngle)

        redComponent.fillWidth = globalArcWidth
        redComponent.strokeWidth = globalStrokeWidth
        redComponent.indicatorStrokeWidth = globalIndicatorStrokeWidth
        redComponent.indicatorStrokeColor = globalIndicatorStrokeColor
        redComponent.strokeColor = globalStrokeColor
        redComponent.indicatorRadius = globalIndicatorRadius

        redRadiusOffset = if (globalRadiusOffset == 0f) dp(1f) else globalRadiusOffset

    }

    override fun onDraw(canvas: Canvas) {
        redComponent.drawComponent(canvas)
    }

    override fun onSizeChanged(width: Int, height: Int, oldW: Int, oldH: Int) {
        val minimumSize = if (width > height) height else width
        val padding = (paddingLeft + paddingRight + paddingTop + paddingBottom) / 4f
        val outerRadius = minimumSize.toFloat() / 2f - padding

        redComponent.setRadius(outerRadius, redRadiusOffset)

        metrics.centerX = width / 2f
        metrics.centerY = height / 2f

        redComponent.updateComponent(redComponent.angle)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val isTouched = redComponent.onTouchEvent(event)
        invalidate()
        return isTouched
    }

    fun setColorSelectionListener(listener: OnColorSelectionListener) {
        redComponent.setColorSelectionListener(listener)
    }

    open fun setColor(color: Int) {
        val red = Color.red(color).toFloat()
        metrics.color[0] = red
        redComponent.updateAngle(red)
        invalidate()
    }

    private fun dp(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)
    }


    override fun onSaveInstanceState(): Parcelable? {
        val bundle = super.onSaveInstanceState()
        if (bundle != null) {
            return SavedState(bundle).apply {
                color = ColorUtils.HSLToColor(metrics.color)
            }
        }
        return null
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
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
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