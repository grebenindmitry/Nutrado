package io.github.grebenindmitry.nutrado;

import com.google.gson.annotations.SerializedName;

public class ProductResponse {
    @SerializedName("product")
    private Product product;
    @SerializedName("status")
    private int status;

    public ProductResponse(Product product, int status) {
        this.product = product;
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
