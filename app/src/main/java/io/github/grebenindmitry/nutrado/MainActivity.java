package io.github.grebenindmitry.nutrado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "nutrado-main";

    private void oof() {
        final MyDAO dao = MyDatabase.getDatabase(getApplicationContext()).myDAO();
        RecyclerView listsRecyclerView = findViewById(R.id.nav_list_recycler);
        List<ProductList> lists = dao.getLists();
        runOnUiThread(() -> listsRecyclerView.setAdapter(new ListsAdapter(lists)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.getBoolean("overrideDarkMode", false)) {
            AppCompatDelegate.setDefaultNightMode(preferences.getBoolean("darkMode", false) ?
                    AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        }

        Toolbar toolbar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(toolbar);

        ((RecyclerView) findViewById(R.id.nav_list_recycler)).setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Executors.newSingleThreadExecutor().execute(this::oof);

        toolbar.setNavigationOnClickListener(
                v -> ((DrawerLayout) findViewById(R.id.drawerLayout)).open()
        );

        RecyclerView productsRecyclerView = findViewById(R.id.recycler_view);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        OpenFoodFactsLoader loader = new OpenFoodFactsLoader(getApplicationContext(), "https://world.openfoodfacts.org");
        loader.setRecyclerAdapter(
                preferences.getInt("prodNum", 100),
                preferences.getString("prodCategory", "plant-based-foods-and-beverages"),
                productsRecyclerView,
                findViewById(R.id.recycler_progress_bar)
        );

        ((SwipeRefreshLayout) findViewById(R.id.swipe_refresh)).setOnRefreshListener(() -> {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        } else if (id == R.id.action_add_list) {
            Executors.newSingleThreadExecutor().execute(() -> {
                final MyDAO dao = MyDatabase.getDatabase(getApplicationContext()).myDAO();
                dao.insertList(new ProductList("oof", "fuck it"));
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}