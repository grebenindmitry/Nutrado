package io.github.grebenindmitry.nutrado;

import com.google.gson.annotations.SerializedName;
import java.util.Vector;

public class OpenFoodFactsSearch {
    @SerializedName("products")
    private Vector<Product> products;

    public OpenFoodFactsSearch(Vector<Product> products) {
        this.products = products;
    }

    public Vector<Product> getProducts() {
        return products;
    }

    public void setProducts(Vector<Product> products) {
        this.products = products;
    }
}
