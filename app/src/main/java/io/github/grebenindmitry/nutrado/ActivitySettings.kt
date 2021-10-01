package io.github.grebenindmitry.nutrado

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.appbar.MaterialToolbar

class ActivitySettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener { finish() }

        val fragmentCompat = FragmentSettings()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, fragmentCompat)
            .commit()
    }
}