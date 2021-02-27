package io.github.grebenindmitry.nutrado;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AddProductActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(this, "Scanner unavailable without Camera permission", Toast.LENGTH_LONG).show();
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //if got the camera permission
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            CodeScannerView scannerView = findViewById(R.id.scanner_view);
            mCodeScanner = new CodeScanner(this, scannerView);
            mCodeScanner.setDecodeCallback(result ->
                    runOnUiThread(() -> {
                        ((TextView) findViewById(R.id.input)).setText(result.getText());
                        findViewById(R.id.save_btn).performClick();
                    }));
            scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
            mCodeScanner.startPreview();
        //otherwise request the permission
        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA);
        }

        //attempt to fetch and save the product
        findViewById(R.id.save_btn).setOnClickListener((view) -> {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(new StringRequest(
                    "https://world.openfoodfacts.org/api/v0/product/" + ((TextView) findViewById(R.id.input)).getText() + ".json",
                    response -> Executors.newSingleThreadExecutor().execute(() -> {
                        ProductResponse productResponse = new Gson().fromJson(response, ProductResponse.class);
                        //if found the product
                        if (productResponse.getStatus() == 1) {
                            Product product = productResponse.getProduct();

                            final ProductDAO productDAO = MyDatabase.getDatabase(this).productDAO();
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
                                            productDAO.insert(product);
                                            listWithProductsDAO.addProductToList(new ProductListCrossref(product.getProductId(), singleIds.get(which)));
                                            mCodeScanner.startPreview();
                                        }))
                                        .show();
                            });
                        //if not found the product
                        } else {
                            runOnUiThread(() -> Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show());
                        }
                    }),
                    error -> {
                        runOnUiThread(() -> Toast.makeText(this, "Could not connect to the server", Toast.LENGTH_SHORT).show());
                    }));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start scanning
        try {
            mCodeScanner.startPreview();
        } catch (NullPointerException ignored) {}
    }

    @Override
    protected void onPause() {
        //stop scanning
        try {
            mCodeScanner.releaseResources();
        } catch (NullPointerException ignored) {}
        super.onPause();
    }
}
