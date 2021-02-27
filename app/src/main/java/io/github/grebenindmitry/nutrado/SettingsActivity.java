package io.github.grebenindmitry.nutrado;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        //make the back button kill the activity
        toolbar.setNavigationOnClickListener((view) -> finish());

        SettingsFragment fragmentCompat = new SettingsFragment();
        //load the settings fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, fragmentCompat)
                .commit();
    }
}
