package com.grnl.gpstracker.fragments

import android.os.Bundle
import android.util.Log
import androidx.core.graphics.drawable.DrawableCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.grnl.gpstracker.R

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var timePreference: Preference
    private lateinit var colorPreference: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
        init()
    }

    private fun init() {
        try {
            Log.d("MyAppTag", "Finding preferences by keys")
            timePreference = findPreference("update_time_key")!!
            colorPreference = findPreference("color_key")!!

            updatePreferenceTitle(timePreference, R.array.loc_time_update_names, R.array.loc_time_update_values)
            timePreference.onPreferenceChangeListener = onChangeListener(R.array.loc_time_update_names, R.array.loc_time_update_values)

            updatePreferenceTitle(colorPreference, R.array.color_names, R.array.color_values)
            colorPreference.onPreferenceChangeListener = onColorChangeListener()

        } catch (e: Exception) {
            Log.e("MyAppTag", "An error occurred: ${e.message}", e)
        }
    }

    private fun onChangeListener(nameArrayResId: Int, valueArrayResId: Int): Preference.OnPreferenceChangeListener {
        return Preference.OnPreferenceChangeListener { pref, value ->
            val nameArray = resources.getStringArray(nameArrayResId)
            val valueArray = resources.getStringArray(valueArrayResId)

            val index = valueArray.indexOf(value)
            if (index >= 0) {
                val title = pref.title.toString().substringBefore(":")
                pref.title = "$title: ${nameArray[index]}"
            } else {
                Log.e("MyAppTag", "Value not found in valueArray")
            }
            true
        }
    }

    private fun onColorChangeListener(): Preference.OnPreferenceChangeListener {
        return Preference.OnPreferenceChangeListener { pref, value ->
            val colorNames = resources.getStringArray(R.array.color_names)
            val colorValues = resources.getStringArray(R.array.color_values)

            val index = colorValues.indexOf(value)
            if (index >= 0) {
                val title = pref.title.toString().substringBefore(":")
                pref.title = "$title: ${colorNames[index]}"
                updatePreferenceIcon(pref, colorValues[index])
            } else {
                Log.e("MyAppTag", "Value not found in colorValues")
            }
            true
        }
    }

    private fun updatePreferenceTitle(preference: Preference, nameArrayResId: Int, valueArrayResId: Int) {
        val value = preference.sharedPreferences?.getString(preference.key, "")
        if (value != null) {
            val nameArray = resources.getStringArray(nameArrayResId)
            val valueArray = resources.getStringArray(valueArrayResId)
            val index = valueArray.indexOf(value)
            if (index >= 0) {
                val title = preference.title.toString().substringBefore(":")
                preference.title = "$title: ${nameArray[index]}"
            }
            if (preference.key == "color_key") {
                updatePreferenceIcon(preference, value)
            }
        }
    }

    private fun updatePreferenceIcon(preference: Preference, colorValue: String) {
        try {
            val color = android.graphics.Color.parseColor(colorValue)
            val drawable = preference.icon?.let { DrawableCompat.wrap(it) }
            if (drawable != null) {
                DrawableCompat.setTint(drawable, color)
                preference.icon = drawable
            } else {
                Log.e("MyAppTag", "Drawable is null")
            }
        } catch (e: IllegalArgumentException) {
            Log.e("MyAppTag", "Invalid color value: $colorValue", e)
        }
    }
}
