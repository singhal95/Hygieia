package app.project.com.hygieia

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by nitinsinghal on 11/04/19.
 */
class PreferenceUtilities {

    val KEY_WATER_COUNT = "water-count"
    val KEY_CHARGING_REMINDER_COUNT = "charging-reminder-count"
    val KEY_REMINDER_TIME_INTERVAL_SETTINGS = "reminder_interval_time"

    private val DEFAULT_COUNT = 0
    private val DEFAULT_REMINDER_TIME_INTERVAL_SETTINGS_TIME = "15"


    companion object {

        val KEY_WATER_COUNT = "water-count"
        val KEY_CHARGING_REMINDER_COUNT = "charging-reminder-count"
        val KEY_REMINDER_TIME_INTERVAL_SETTINGS = "reminder_interval_time"
        private val DEFAULT_COUNT = 0
        private val DEFAULT_REMINDER_TIME_INTERVAL_SETTINGS_TIME = "15"
        fun setWaterCount(context: Context, glassesOfWater: Int) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()
            editor.putInt(KEY_WATER_COUNT, glassesOfWater)
            editor.apply()
        }


        fun getWaterCount(context: Context): Int {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getInt(KEY_WATER_COUNT, DEFAULT_COUNT)
        }
    }


    @Synchronized
    fun incrementWaterCount(context: Context) {
        var waterCount = getWaterCount(context)
        setWaterCount(context, ++waterCount)
    }

    fun resetWaterCount(context: Context) {
        setWaterCount(context, 0)
    }

    fun resetChargingReminderCount(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        val editor = prefs.edit()
        editor.putInt(KEY_CHARGING_REMINDER_COUNT, 0)
        editor.apply()
    }

    @Synchronized
    fun incrementChargingReminderCount(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        var chargingReminders = prefs.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT)

        val editor = prefs.edit()
        editor.putInt(KEY_CHARGING_REMINDER_COUNT, ++chargingReminders)
        editor.apply()
    }

    fun setDefaultSharedPreferenceReminderTime(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(KEY_REMINDER_TIME_INTERVAL_SETTINGS, DEFAULT_REMINDER_TIME_INTERVAL_SETTINGS_TIME)
        editor.apply()
    }
}