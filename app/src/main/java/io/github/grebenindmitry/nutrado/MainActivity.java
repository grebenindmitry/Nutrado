package io.github.grebenindmitry.nutrado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "nutrado-main";
    private ScheduledExecutorService repeater;
    private int selectedList = -1;
    private SharedPreferences preferences = null;

    private void networkCheck() {
        //if network is unavailable load the offline data
        NetworkInfo activeNetwork = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("offline", true);
            //if selected online list change it to offline
            if (preferences.getInt("selectedList", -1) == -1) {
                editor.putInt("selectedList", -2).commit();
            }
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("offline", false);
            //if selected offline list change it to online
            if (preferences.getInt("selectedList", -1) == -2) {
                editor.putInt("selectedList", -1).commit();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check if network changed since oncreate
        networkCheck();

        populateNavigation();

        if (selectedList == -1) {
            populateMainListFromOFF();
        } else {
            populateMainListFromDB();
        }

        repeater = Executors.newSingleThreadScheduledExecutor();
        repeater.scheduleAtFixedRate(() -> {
            //if list was changed reload the activity
            if (selectedList != this.preferences.getInt("selectedList", -1))
                reloadActivity();
        }, 0, 500, TimeUnit.MILLISECONDS);

        //if override enabled, set the theme to selected
        if (preferences.getBoolean("overrideDarkMode", false)) {
            AppCompatDelegate.setDefaultNightMode(this.preferences.getBoolean("darkMode", false) ?
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

    private void populateNavigation() {
        Executors.newSingleThreadExecutor().execute(() -> {
            final ListDAO dao = MyDatabase.getDatabase(this).listDAO();
            RecyclerView listsRecyclerView = findViewById(R.id.nav_list_recycler);
            List<ProductList> lists = dao.getLists();
            runOnUiThread(() -> listsRecyclerView.setAdapter(new ListsAdapter((List<ProductList>) lists)));
        });
    }

    private void populateMainListFromDB() {
        Executors.newSingleThreadExecutor().execute(() -> {
            final ListWithProductsDAO dao = MyDatabase.getDatabase(this).listWithProductsDAO();
            RecyclerView productsRecyclerView = findViewById(R.id.recycler_view);
            ProgressBar progressBar = findViewById(R.id.recycler_progress_bar);

            //set progressbar to indeterminate (and do it while it is invisible)
            runOnUiThread(() -> {
                progressBar.setVisibility(View.INVISIBLE);
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
            });
            List<Product> newProductList = dao.getListWithProducts(selectedList).products;
            runOnUiThread(() -> {
                productsRecyclerView.setAdapter(new ProductAdapter(newProductList));
                productsRecyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            });
        });
    }

    private void populateMainListFromOFF() {
        OpenFoodFactsLoader loader = new OpenFoodFactsLoader(this, "https://world.openfoodfacts.org");
        loader.setRecyclerAdapter(
                this.preferences.getInt("prodNum", 100),
                this.preferences.getString("prodCategory", "plant-based-foods-and-beverages"),
                findViewById(R.id.recycler_view),
                findViewById(R.id.recycler_progress_bar)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        networkCheck();

        //load the list preference
        this.selectedList = preferences.getInt("selectedList", -1);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener((view) -> {
            startActivity(new Intent(this, AddProductActivity.class));
        });

        //open sidebar when burger button pressed
        toolbar.setNavigationOnClickListener(v -> ((DrawerLayout) findViewById(R.id.drawerLayout)).open());

        findViewById(R.id.add_list_btn).setOnClickListener((view) ->
                startActivity(new Intent(this, CreateListActivity.class)));

        ((SwipeRefreshLayout) findViewById(R.id.swipe_refresh)).setOnRefreshListener(this::reloadActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (this.preferences.getInt("selectedList", -1) == -1) {
            menu.findItem(R.id.action_delete_list).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_delete_list) {
            //create a dialog to confirm deletion of list
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.delete_list)
                    .setMessage(R.string.delete_list_msg)
                    .setPositiveButton(R.string.yes, (dialog, which) ->
                            Executors.newSingleThreadExecutor().execute(() -> {
                                final ListDAO listDAO = MyDatabase.getDatabase(this).listDAO();
                                listDAO.delete(listDAO.getList(selectedList));
                                //change the selected list away from the deleted one and reload
                                runOnUiThread(() -> {
                                    preferences.edit().putInt("selectedList", -1).commit();
                                    reloadActivity();
                                });
                    }))
                    .setNeutralButton(R.string.cancel, ((dialog, which) -> {}))
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //kill the scheduler when the activity is out of view
        repeater.shutdown();
    }
}