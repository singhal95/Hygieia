package app.project.com.hygieia

import android.content.Context
import android.content.SharedPreferences
import android.preference.DialogPreference
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.NumberPicker

/**
 * Created by nitinsinghal on 11/04/19.
 */
class NumberPickerPreference (context: Context, attrs: AttributeSet): DialogPreference(context,attrs) {

    private var numberPicker: NumberPicker? = null
    private var mText: String? = null
    private var mTextSet: Boolean = false




    override fun onCreateDialogView(): View {
        return generateNumberPicker()
    }

    fun generateNumberPicker(): NumberPicker {
        numberPicker = NumberPicker(context)
        numberPicker!!.minValue = 15
        numberPicker!!.maxValue = 60
        numberPicker!!.value = 15

        /*
         * Anything else you want to add to this.
         */
        return numberPicker as NumberPicker
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        super.onDialogClosed(positiveResult)
        if (positiveResult) {
            val value = numberPicker!!.value
            Log.d("NumberPickerPreference", "NumberPickerValue : " + value)
            persistString(value.toString())
            if (callChangeListener(value)) {
                setText(value.toString())
            }
        }
    }

    /**
     * Saves the text to the [SharedPreferences].
     *
     * @param text The text to save
     */
    fun setText(text: String) {
        // Always persist/notify the first time.
        val changed = !TextUtils.equals(mText, text)
        if (changed || !mTextSet) {
            mText = text
            mTextSet = true
            persistString(text)
            if (changed) {
                notifyDependencyChange(shouldDisableDependents())
                notifyChanged()
            }
        }
    }
}

