package io.github.grebenindmitry.nutrado

import androidx.preference.PreferenceFragmentCompat
import android.os.Bundle

class FragmentSettings : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}