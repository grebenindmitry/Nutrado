package io.github.grebenindmitry.nutrado;

import com.google.gson.annotations.SerializedName;

public class OpenFoodFactsSearch {
    @SerializedName("products")
    private Product[] products;

    public OpenFoodFactsSearch(Product[] products) {
        this.products = products;
    }

    public Product[] getProducts() {
        return products;
    }

    public void setProducts(Product[] products) {
        this.products = products;
    }
}
