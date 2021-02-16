package io.github.grebenindmitry.nutrado;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(toolbar);

        RecyclerView listsRecyclerView = findViewById(R.id.nav_list_recycler);
        listsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Executors.newSingleThreadExecutor().execute(() -> {
            final MyDAO dao = MyDatabase.getDatabase(getApplicationContext()).myDAO();
            List<ProductList> lists = dao.getLists();
            runOnUiThread(() -> {
                listsRecyclerView.setAdapter(new ListsAdapter(lists));
            });
        });

        toolbar.setNavigationOnClickListener(v -> {
            DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
            if (drawerLayout.isOpen()) {
                drawerLayout.close();
            } else {
                drawerLayout.open();
            }
        });

        RecyclerView productsRecyclerView = findViewById(R.id.recycler_view);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        OpenFoodFactsLoader loader = new OpenFoodFactsLoader(getApplicationContext(), "https://world.openfoodfacts.org");
        loader.setRecyclerAdapter(100, "plant-based-foods-and-beverages", productsRecyclerView, findViewById(R.id.recycler_progress_bar));

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}