package com.madrapps.pikolo

import android.content.Context
import android.graphics.Canvas
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.madrapps.pikolo.listeners.OnColorSelectionListener

abstract class ColorPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    protected val paints = Paints()
    protected val config: Config

    internal abstract val color: Int

    abstract override fun onDraw(canvas: Canvas)
    abstract override fun onSizeChanged(width: Int, height: Int, oldW: Int, oldH: Int)
    abstract fun setColorSelectionListener(listener: OnColorSelectionListener)
    abstract fun setColor(color: Int)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPicker, defStyleAttr, 0)

        val arcWidth = typedArray.getDimension(R.styleable.ColorPicker_arc_width, dp(5f))
        val strokeWidth = typedArray.getDimension(R.styleable.ColorPicker_stroke_width, 0f)
        val indicatorRadius = typedArray.getDimension(R.styleable.ColorPicker_indicator_radius, dp(15f))
        val indicatorStrokeWidth = typedArray.getDimension(R.styleable.ColorPicker_indicator_stroke_width, dp(2f))
        val strokeColor = typedArray.getColor(R.styleable.ColorPicker_stroke_color, 0)
        val indicatorStrokeColor = typedArray.getColor(R.styleable.ColorPicker_indicator_stroke_color, 0)
        val arcLength = typedArray.getFloat(R.styleable.ColorPicker_arc_length, 0f)
        val radiusOffset = typedArray.getDimension(R.styleable.ColorPicker_radius_offset, 0f)

        typedArray.recycle()

        config = Config(arcWidth, strokeWidth, indicatorRadius, indicatorStrokeWidth,
                strokeColor, indicatorStrokeColor, arcLength, radiusOffset)
    }

    protected fun dp(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = super.onSaveInstanceState()
        if (bundle != null) {
            return SavedState(bundle).apply {
                color = this@ColorPicker.color
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

    data class Config(val arcWidth: Float,
                      val strokeWidth: Float,
                      val indicatorRadius: Float,
                      val indicatorStrokeWidth: Float,
                      val strokeColor: Int,
                      val indicatorStrokeColor: Int,
                      val arcLength: Float,
                      val radiusOffset: Float)
}