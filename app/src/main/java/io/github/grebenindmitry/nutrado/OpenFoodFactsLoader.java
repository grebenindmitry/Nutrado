package io.github.grebenindmitry.nutrado;

import android.content.Context;
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
import java.util.concurrent.Executors;

public class OpenFoodFactsLoader {
    private final String url;
    private final Context context;
    private final RequestQueue requestQueue;
    private final Vector<Product> products;
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
        this.retriesNum = 0;
    }

    public void setRecyclerAdapter(int prodNum, String category, RecyclerView recyclerView, ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.prodNum = prodNum;
        this.recyclerView = recyclerView;
        //set up the progressbar
        progressBar.setMax(prodNum);
        progressBar.setProgress(0);

        //start the request loop, which will run until there are prodNum full products
        request = new StringRequest(url + "/category/" + category + ".json?page=" + this.pageNum,
                this::handleResponse,
                this::handleError);
        this.requestQueue.add(request);
    }

    private void handleResponse(final String response) {
        //reset the retry counter
        retriesNum = 0;
        OpenFoodFactsSearch openFoodFactsSearch = new Gson().fromJson(response, OpenFoodFactsSearch.class);
        for (Product product : openFoodFactsSearch.getProducts()) {
            //if product full add it
            if (product.isFull()) {
                this.products.add(product);
            }
            //base case of recursion
            if (this.products.size() == prodNum) {
                //set the adapter
                this.recyclerView.setAdapter(new ProductAdapter(products));
                final ProductDAO productDAO = MyDatabase.getDatabase(context).productDAO();
                final ListWithProductsDAO listWithProductsDAO = MyDatabase.getDatabase(context).listWithProductsDAO();
                //add all the products to the offline list
                for (Product product1 : products) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        productDAO.insert(product1);
                        listWithProductsDAO.addProductToList(new ProductListCrossref(product1.getProductId(), -2));
                    });
                }
                this.recyclerView.setVisibility(View.VISIBLE);
                this.progressBar.setVisibility(View.INVISIBLE);
                break;
            }
        }
        //if there aren't enough products increment pageNum and start another request
        if (this.products.size() < this.prodNum) {
            this.pageNum++;
            this.requestQueue.add(request);
        }
        //update the progressbar
        progressBar.setProgress(products.size(), true);
    }

    private void handleError(VolleyError error) {
        //if there have been less than 3 retries, try again
        if (retriesNum < 2) {
            retriesNum++;
            this.requestQueue.add(request);
        } else {
            //otherwise print a message
            Toast.makeText(this.context,
                    "Connection to server was interrupted. Data may be incomplete",
                    Toast.LENGTH_LONG).show();
            //if no products were downloaded add a dummy error one
            if (this.products.size() == 0) {
                products.add(new Product("0",
                        "error",
                        -2,
                        "",
                        "",
                        "",
                        ""));
            }
            //set the adapter
            this.recyclerView.setAdapter(new ProductAdapter(this.products));
            this.recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
