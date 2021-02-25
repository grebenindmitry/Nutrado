package io.github.grebenindmitry.nutrado;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "nutrado-main";
    private ScheduledExecutorService repeater;
    private int selectedList = -1;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        repeater = Executors.newSingleThreadScheduledExecutor();
        repeater.scheduleAtFixedRate(() -> {
            if (preferences.getInt("selectedList", -1) != selectedList)
                reloadActivity();
        }, 500, 500, TimeUnit.MILLISECONDS);

        //if override enabled, set the theme to selected
        if (preferences.getBoolean("overrideDarkMode", false)) {
            AppCompatDelegate.setDefaultNightMode(preferences.getBoolean("darkMode", false) ?
                    AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        //else follow system
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    private void reloadActivity() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void reloadActivity(View view) {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void populateNavigation() {
        Executors.newSingleThreadExecutor().execute(() -> {
            final ListDAO dao = MyDatabase.getDatabase(this).listDAO();
            RecyclerView listsRecyclerView = findViewById(R.id.nav_list_recycler);
            List<ProductList> lists = dao.getLists();
            runOnUiThread(() -> listsRecyclerView.setAdapter(new ListsAdapter(lists)));
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.selectedList = preferences.getInt("selectedList", -1);

        Toolbar toolbar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(toolbar);

        //load the list of lists
        populateNavigation();

        //open sidebar when burger button pressed
        toolbar.setNavigationOnClickListener(
                v -> ((DrawerLayout) findViewById(R.id.drawerLayout)).open()
        );

        //if selected online list, load from OFF API
        if (selectedList == -1) {
            OpenFoodFactsLoader loader = new OpenFoodFactsLoader(this, "https://world.openfoodfacts.org");
            loader.setRecyclerAdapter(
                    preferences.getInt("prodNum", 100),
                    preferences.getString("prodCategory", "plant-based-foods-and-beverages"),
                    findViewById(R.id.recycler_view),
                    findViewById(R.id.recycler_progress_bar)
            );
        //else load from db
        } else if (selectedList >= 0) {
            Executors.newSingleThreadExecutor().execute(() -> {
                final ListWithProductsDAO dao = MyDatabase.getDatabase(this).listWithProductsDAO();
                RecyclerView productsRecyclerView = findViewById(R.id.recycler_view);
                List<Product> productList = dao.getListWithProducts(selectedList).products;
                productsRecyclerView.setAdapter(new ProductAdapter(productList));
                productsRecyclerView.setVisibility(View.VISIBLE);
                findViewById(R.id.recycler_progress_bar).setVisibility(View.INVISIBLE);
            });
        }

        findViewById(R.id.add_list_btn).setOnClickListener((view) -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                final ListDAO dao = MyDatabase.getDatabase(this).listDAO();
                List<ProductList> lists = dao.getLists();
                String name = "";
                if (!lists.isEmpty()) {
                    name = String.valueOf(lists.get(lists.size() - 1).getListId());
                }
                dao.insert(new ProductList("oof" + name, "fuck it", Color.valueOf(Color.RED), R.drawable.ic_outline_bookmark_24));
                populateNavigation();
            });
        });

        //reload activity on swipe down
        ((SwipeRefreshLayout) findViewById(R.id.swipe_refresh)).setOnRefreshListener(this::reloadActivity);
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

        //start settings activity
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_read_db) {
            Executors.newSingleThreadExecutor().execute(() -> {
                final ListWithProductsDAO dao = MyDatabase.getDatabase(this).listWithProductsDAO();
                String oof = "";
                for (ListWithProducts listWithProducts : dao.getListsWithProducts()) {
                    oof += "List name: " + listWithProducts.list.getName() + "\n";
                    for (Product product : listWithProducts.products) {
                        oof += product.getName() + "\n";
                    }
                }
                String finalOof = oof;
                runOnUiThread(() -> Toast.makeText(this, finalOof, Toast.LENGTH_LONG).show());
            });
        } else if (id == R.id.action_test) {
            Log.d(TAG, "onOptionsItemSelected: test");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        repeater.shutdown();
    }
}