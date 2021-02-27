package io.github.grebenindmitry.nutrado;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ProductActivity extends AppCompatActivity {
    private final String TAG = "prod-activity";

    private void reloadActivity() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //make the back button kill the activity
        toolbar.setNavigationOnClickListener((view) -> finish());

        //get the product id passed from the main activity
        String prodId = getIntent().getExtras().getString("id");

        Executors.newSingleThreadExecutor().execute(() -> {
            final ProductDAO productDAO = MyDatabase.getDatabase(this).productDAO();
            final ProductInListsDAO productInListsDAO = MyDatabase.getDatabase(this).productInListsDAO();
            //if the item is in some list, make it deletable
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

                ((TextView) findViewById(R.id.nutrition_grade)).setText(product.getGrade());
                ((TextView) findViewById(R.id.ingredients)).setText(product.getIngredients());

                deleteButton.setEnabled(deletable);
                deleteButton.setOnClickListener((v -> Executors.newSingleThreadExecutor().execute(() -> {
                    //delete the product and kill activity
                    productDAO.delete(product);
                    finish();
                })));

                saveButton.setEnabled(true);
                saveButton.setOnClickListener((view) -> Executors.newSingleThreadExecutor().execute(() -> {
                    final ListDAO listDAO = MyDatabase.getDatabase(this).listDAO();
                    final ListWithProductsDAO listWithProductsDAO = MyDatabase.getDatabase(this).listWithProductsDAO();

                    List<ProductList> productLists = listDAO.getLists();
                    ArrayList<String> singleItems = new ArrayList<>();
                    ArrayList<Integer> singleIds = new ArrayList<>();

                    //save the list ids and names (excluding the online/offline) in suitable structures
                    for (int i = 0; i < productLists.size(); i++) {
                        if (productLists.get(i).getListId() > 0) {
                            singleItems.add(productLists.get(i).getName());
                            singleIds.add(productLists.get(i).getListId());
                        }
                    }

                    runOnUiThread(() -> {
                        CharSequence[] names = new CharSequence[singleItems.size()];
                        names = singleItems.toArray(names);
                        //create a dialog with the lists
                        new MaterialAlertDialogBuilder(this)
                                .setTitle(this.getString(R.string.select_list))
                                .setItems(names, (dialog, which) -> Executors.newSingleThreadExecutor().execute(() -> {
                                    //add to list and reload to show the delete button
                                    listWithProductsDAO.addProductToList(new ProductListCrossref(product.getProductId(), singleIds.get(which)));
                                    reloadActivity();
                                }))
                                .show();
                    });
                }));
            });
        });
    }
}
