package com.grnl.gpstracker.fragments

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.grnl.gpstracker.R

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var timePreference: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
        init()
    }

    private fun init() {
        try {
            Log.d("MyAppTag", "Finding preference by key: update_time_key")
            val preference = findPreference<Preference>("update_time_key")
            val colorPreference = findPreference<Preference>("color_key")
            if (preference == null) {
                Log.e("PREFERENCE:", "Preference not found")
            } else {
                timePreference = preference
                updatePreferenceTitle(timePreference)
                val changeListener = onChangeListener()
                timePreference.onPreferenceChangeListener = changeListener

            }
        } catch (e: Exception) {
            Log.e("MyAppTag", "An error occurred: ${e.message}", e)
        }
    }

    private fun onChangeListener(): Preference.OnPreferenceChangeListener  {
        return Preference.OnPreferenceChangeListener{
            pref, value ->
//                Toast.makeText(context, "CHANGED $value", Toast.LENGTH_LONG).show()
            val nameArray = resources.getStringArray(R.array.loc_time_update_names)
            val valueArray = resources.getStringArray(R.array.loc_time_update_values)

            val title = pref.title.toString().substringBefore(":")
            pref.title = "$title: ${nameArray[valueArray.indexOf(value)]}"

            true
        }
    }

    private fun updatePreferenceTitle(preference: Preference) {
        val value = preference.sharedPreferences?.getString(preference.key, "")
        if (value != null) {
            val nameArray = resources.getStringArray(R.array.loc_time_update_names)
            val valueArray = resources.getStringArray(R.array.loc_time_update_values)
            val index = valueArray.indexOf(value)
            if (index >= 0) {
                val title = preference.title.toString().substringBefore(":")
                preference.title = "$title: ${nameArray[index]}"
            }
        }
    }



}