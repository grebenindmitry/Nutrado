package io.github.grebenindmitry.nutrado;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProductActivity extends AppCompatActivity {
    private final String TAG = "prod-activ";

    private void reloadActivity() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_product_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener((view) -> finish());

        String prodId = getIntent().getExtras().getString("id");

        Executors.newSingleThreadExecutor().execute(() -> {
            final ProductDAO productDAO = MyDatabase.getDatabase(this).productDAO();
            final ProductInListsDAO productInListsDAO = MyDatabase.getDatabase(this).productInListsDAO();
            boolean deletable = !productInListsDAO.getProductInLists(prodId).lists.isEmpty();
            Product product = productDAO.getProduct(prodId);
            runOnUiThread(() -> {
                MaterialButton saveButton = findViewById(R.id.save_btn);
                ImageView prodImage = findViewById(R.id.product_image);
                MaterialButton deleteButton = findViewById(R.id.delete_btn);

                toolbar.setTitle(product.getName());

                Glide.with(this)
                    .load(product.getImageUrl())
                    .circleCrop()
                    .into(prodImage);

                ((TextView) findViewById(R.id.nutrition_grade)).setText(getString(R.string.nutrition_grade_txt, product.getGrade().toUpperCase()));
                deleteButton.setEnabled(deletable);
                deleteButton.setOnClickListener((v -> Executors.newSingleThreadExecutor().execute(() -> {
                    productDAO.delete(product);
                    finish();
                })));
                saveButton.setEnabled(true);
                saveButton.setOnClickListener((view) -> Executors.newSingleThreadExecutor().execute(() -> {
                    final ListDAO listDAO = MyDatabase.getDatabase(this).listDAO();
                    final ListWithProductsDAO listWithProductsDAO = MyDatabase.getDatabase(this).listWithProductsDAO();
                    List<ProductList> productLists = listDAO.getLists();
                    String[] singleItems = new String[productLists.size()];
                    int[] singleIds = new int[productLists.size()];
                    for (int i = 0; i < singleIds.length; i++) {
                        singleItems[i] = productLists.get(i).getName();
                        singleIds[i] = productLists.get(i).getListId();
                    }

                    runOnUiThread(() -> new MaterialAlertDialogBuilder(this)
                            .setTitle(this.getString(R.string.select_list))
                            .setItems(singleItems, (dialog, which) -> Executors.newSingleThreadExecutor().execute(() -> {
                                productDAO.insert(product);
                                listWithProductsDAO.addProductToList(new ProductListCrossref(product.getProductId(), singleIds[which]));
                                reloadActivity();
                            }))
                            .show()
                    );
                }));
            });
        });
    }
}
