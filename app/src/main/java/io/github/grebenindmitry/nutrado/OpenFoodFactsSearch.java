package io.github.grebenindmitry.nutrado;

import com.google.gson.annotations.SerializedName;

public class OpenFoodFactsSearch {
    @SerializedName("products")
    private ProductData[] products;

    public OpenFoodFactsSearch(ProductData[] products) {
        this.products = products;
    }

    public ProductData[] getProducts() {
        return products;
    }

    public void setProducts(ProductData[] products) {
        this.products = products;
    }
}
