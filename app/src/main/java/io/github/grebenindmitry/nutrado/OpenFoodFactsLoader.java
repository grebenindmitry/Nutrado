package io.github.grebenindmitry.nutrado;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.util.Vector;

public class OpenFoodFactsLoader {
    private final String url;
    private final Context context;
    private final RequestQueue requestQueue;
    private Vector<Product> products;
    private StringRequest request;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private int prodNum;
    private int pageNum;
    private int retriesNum;

    public OpenFoodFactsLoader(Context context, String url) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.url = url;
        this.pageNum = 1;
        this.products = new Vector<>();
        retriesNum = 0;
    }

    public void setRecyclerAdapter(int prodNum, String category, RecyclerView recyclerView, ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.prodNum = prodNum;
        this.recyclerView = recyclerView;
        progressBar.setMax(prodNum);
        progressBar.setProgress(0);

        request = new StringRequest(url + "/category/" + category + ".json?page=" + this.pageNum,
                this::handleResponse,
                this::handleError);
        this.requestQueue.add(request);
    }

    private void handleResponse(final String response) {
        retriesNum = 0;
        OpenFoodFactsSearch openFoodFactsSearch = new Gson().fromJson(response, OpenFoodFactsSearch.class);
        for (Product product : openFoodFactsSearch.getProducts()) {
            if (product.isFull()) {
                this.products.add(product);
            }
            if (this.products.size() == prodNum) {
                this.recyclerView.setAdapter(new ProductAdapter(products));
                this.recyclerView.setVisibility(View.VISIBLE);
                this.progressBar.setVisibility(View.INVISIBLE);
                break;
            }
        }
        if (this.products.size() < this.prodNum) {
            this.pageNum++;
            this.requestQueue.add(request);
        }
        progressBar.setProgress(products.size(), true);
    }

    private void handleError(VolleyError error) {
        if (retriesNum < 2) {
            retriesNum++;
            this.requestQueue.add(request);
        } else {
            Log.e("off-loader", "failed to receive json after 3 tries");
            Toast.makeText(this.context,
                    "Connection to server was interrupted. Data may be incomplete",
                    Toast.LENGTH_LONG).show();
            if (this.products.size() == 0) {
                products.add(new Product("0",
                        "error",
                        -2,
                        "",
                        "",
                        ""));
            }
            this.recyclerView.setAdapter(new ProductAdapter(this.products));
            this.recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
